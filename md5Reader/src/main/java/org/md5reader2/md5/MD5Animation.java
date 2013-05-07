/**
 * 
 */
package org.md5reader2.md5;

import org.util.Quaternion;
import org.util.Vector3f;

/**
 * @author Marco Frisan, Tim Joergen
 * 
 */
public class MD5Animation {
	private int md5Version;
	private String commandLine;
	private int numFrames;
	private int numJoints;
	private int frameRate;
	private int numAnimatedComponents;

	// Others joint array variables, can be useful...
	private String[] jointName;
	private int[] jointParent;

	// I store in this arrays the flags and start indices data for each joint.
	// TODO: flags could be stored also in a byte array.
	private int[] flags;
	private int[] startIndex;

	private MD5BoundingBox[] boundings;
	private MD5Frame baseFrame;
	
	int frameNumber = 0;

    // I store in this arrays the flags and start indices data for each joint.
    // TODO: flags could be stored also in a byte array.
    private MD5Frame currentFrame;
    private MD5Frame nextFrame;
    private MD5Frame lastFrame;
    private MD5Frame interpolatedFrame = new MD5Frame();

    private MD5Frame[] calculatedFrames;

	// I try a implementation of a per frames architecture like in MD5 format.
	// For more info read MD5Frame documentation.
	//
	// This variable stores all the frames associated with this animation.
	private MD5Frame[] frames;

	/**
	 * 
	 */
	public MD5Animation() {
		super();
	}

	/**
	 * @return the numFrames
	 */
	public int getNumFrames() {
		return numFrames;
	}

	/**
	 * Initializes the number of frames of this <code>MD5Animation</code>.
	 * Note that it also initialize the <code>frames Vector</code>: every
	 * previous stored <code>frames</code> will be discarded.
	 * 
	 * @param numFrames
	 *            the numFrames to set
	 */
	public void setNumFrames(int numFrames) {
		this.numFrames = numFrames;
		
		// TODO: maybe it will be better, in the future, to add another setter
		// that do not discard previous frames.
		
		// Initialize frames array.
		this.frames = new MD5Frame[numFrames];
		
		for (int f = 0; f < numFrames; f++) {
			// Prepares positions and rotations Vector objects and set their
			// sizes.
			Vector3f[] positions = new Vector3f[numJoints];
			Quaternion[] rotations = new Quaternion[numJoints];
			// TODO: we could initialize also positions and rotations elements
			// with Zero vectors and quaternions.
			
			// Initialize a variable to temporary hold the current frames.
			MD5Frame currentFrame = new MD5Frame();
			
			// Sets current frames rotations and positions to previously prepared
			// Vector objects.
			currentFrame.setPositions(positions);
			currentFrame.setRotations(rotations);
			
			// Adds currentFrame to this MD5Animation frames Vector.
			this.frames[f] = currentFrame;
		}
	}

	/**
	 * @return the numJoints
	 */
	public int getNumJoints() {
		return numJoints;
	}

	/**
	 * @param numJoints
	 *            the numJoints to set
	 */
	public void setNumJoints(int numJoints) {
		this.numJoints = numJoints;
	}

	/**
	 * @return the frameRate
	 */
	public int getFrameRate() {
		return frameRate;
	}

	/**
	 * @param frameRate
	 *            the frameRate to set
	 */
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * @return the frames
	 */
	public MD5Frame[] getFrames() {
		return frames;
	}

	/**
	 * @param frames
	 *            the frames to set
	 */
	public void setFrames(MD5Frame[] frames) {
		this.frames = frames;
	}

	/**
	 * @return the flags
	 */
	public int[] getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	public void setFlags(int[] flags) {
		this.flags = flags;
	}

	/**
	 * @return the startIndex
	 */
	public int[] getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	public void setStartIndex(int[] startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the mD5Version
	 */
	public int getMD5Version() {
		return this.md5Version;
	}

	/**
	 * @param version
	 *            the mD5Version to set
	 */
	public void setMD5Version(int version) {
		this.md5Version = version;
	}

	/**
	 * @return the commandLine
	 */
	public String getCommandLine() {
		return commandLine;
	}

	/**
	 * @param commandLine
	 *            the commandLine to set
	 */
	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}

	/**
	 * @return the numAnimatedComponents
	 */
	public int getNumAnimatedComponents() {
		return numAnimatedComponents;
	}

	/**
	 * @param numAnimatedComponents
	 *            the numAnimatedComponents to set
	 */
	public void setNumAnimatedComponents(int numAnimatedComponents) {
		this.numAnimatedComponents = numAnimatedComponents;
	}

	/**
	 * @return the jointName
	 */
	public String[] getJointName() {
		return jointName;
	}

	/**
	 * @param jointName
	 *            the jointName to set
	 */
	public void setJointName(String[] jointName) {
		this.jointName = jointName;
	}

	/**
	 * @return the jointParent
	 */
	public int[] getJointParent() {
		return jointParent;
	}

	/**
	 * @param jointParent
	 *            the jointParent to set
	 */
	public void setJointParent(int[] jointParent) {
		this.jointParent = jointParent;
	}

	/**
	 * @return the boundings
	 */
	public MD5BoundingBox[] getBoundings() {
		return boundings;
	}

	/**
	 * @param boundings
	 *            the boundings to set
	 */
	public void setBoundings(MD5BoundingBox[] boundings) {
		this.boundings = boundings;
	}

	/**
	 * @return the baseFrame
	 */
	public MD5Frame getBaseFrame() {
		return baseFrame;
	}

	/**
	 * @param baseFrame the baseFrame to set
	 */
	public void setBaseFrame(MD5Frame baseFrame) {
		this.baseFrame = baseFrame;
	}

	Quaternion quaternionComputeW(Quaternion q) {
        float t = 1.0f - (q.x * q.x) - (q.y * q.y) - (q.z * q.z);

        if (t < 0.0f) {
            q.w = 0.0f;
        } else {
            q.w = (float) -Math.sqrt(t);
        }

        return q;
    }
    MD5Joint[] jointsNew;

    void frameToModel(MD5Model model, MD5Frame frame) {
        model.setJoints(frame.getJoints());
    }

    void calculateCurrentFrame() {
    }

	public void animate(MD5Model model) {
        if (lastFrame == null) {
            //calculate the animation key frames
            calculatedFrames = new MD5Frame[this.getNumFrames()];
            for (int i = 0; i < calculatedFrames.length; i++) {
                calculatedFrames[i] = calculateFrame(i, model);
            }
            lastFrame = this.calculatedFrames[0];
            nextFrame = this.calculatedFrames[1];
        }

        float elapsed = System.currentTimeMillis() - lastFrame.getFrameStartTime();
        float changeTime = (1.0f / this.getFrameRate()) * 1000;

        if (elapsed >= changeTime) {
            frameNumber++;
            if (frameNumber == model.getAnimation().getNumFrames()) {
                frameNumber = 0;
            }

            int nextFrameNumber = frameNumber + 1;
            nextFrameNumber = nextFrameNumber >= model.getAnimation().getNumFrames() ? 0 : nextFrameNumber;
            lastFrame = nextFrame;
            lastFrame.setFrameStartTime(System.currentTimeMillis());
            //nextFrame = calculateFrame(nextFrameNumber, model);
            nextFrame = this.calculatedFrames[nextFrameNumber];
            elapsed = 0.0f;
        }

        float percentage;
        if (!(changeTime == 0.0f)) {
            percentage = elapsed / changeTime;
        } else {
            percentage = 0.0f;
        }

        currentFrame = interpolate(lastFrame, nextFrame, percentage);

        //update the model
        frameToModel(model, currentFrame);
    }

    public MD5Frame calculateFrame(int frame, MD5Model model) {
        calculateCurrentFrame();
        MD5Frame newFrame = new MD5Frame();
        newFrame.setJoints(new MD5Joint[this.getNumJoints()]);
        newFrame.setFrameStartTime(System.currentTimeMillis());
        Vector3f positions[];
        Quaternion orientations[];

        positions = new Vector3f[this.getNumJoints()];
        orientations = new Quaternion[this.getNumJoints()];

        jointsNew = new MD5Joint[this.getNumJoints()];
        for (int i = 0; i < getNumJoints(); i++) {
            MD5Joint baseJoint = new MD5Joint();
            Quaternion orient = new Quaternion(getBaseFrame().getRotations()[i]);
            baseJoint.setOrient(orient);
            Vector3f position = new Vector3f(getBaseFrame().getPositions()[i]);
            baseJoint.setPos(position);

            Vector3f animatedPos;
            Quaternion animatedOrient;
            int j = 0;

            animatedPos = new Vector3f(baseJoint.getPos().x,
                    baseJoint.getPos().y,
                    baseJoint.getPos().z);
            animatedOrient = new Quaternion(baseJoint.getOrient().x,
                    baseJoint.getOrient().y,
                    baseJoint.getOrient().z,
                    baseJoint.getOrient().w);

            animatedOrient = quaternionComputeW(animatedOrient);

            // NOTE: we assume that this joint's parent has
            // already been calculated, i.e. joint's ID should
            // never be smaller than its parent ID.
            MD5Joint thisJoint = new MD5Joint();
            Vector3f foo = new Vector3f(getFrames()[frame].getPositions()[i].x,
                    getFrames()[frame].getPositions()[i].y,
                    getFrames()[frame].getPositions()[i].z);
            Quaternion bar = new Quaternion(getFrames()[frame].getRotations()[i].x,
                    getFrames()[frame].getRotations()[i].y,
                    getFrames()[frame].getRotations()[i].z,
                    getFrames()[frame].getRotations()[i].w);
            thisJoint.setPos(foo);
            thisJoint.setOrient(bar);

            int parent = getJointParent()[i];

            /* Has parent? */
            if (parent < 0) {
                thisJoint.setPos(animatedPos);
                thisJoint.setOrient(animatedOrient);
            } else {
                MD5Joint parentJoint = new MD5Joint();
                Vector3f foo3 = new Vector3f(positions[parent].x,
                        positions[parent].y,
                        positions[parent].z);
                Quaternion bar3 = new Quaternion(orientations[parent].x,
                        orientations[parent].y,
                        orientations[parent].z,
                        orientations[parent].w);
                parentJoint.setPos(foo3);
                parentJoint.setOrient(bar3);

                Vector3f rpos;
                rpos = MD5Model.rotatePoint(parentJoint.getOrient(), animatedPos);
//                thisJoint.getPos().x = rpos.x + parentJoint.getPos().x;
//                thisJoint.getPos().y = rpos.y + parentJoint.getPos().y;
//                thisJoint.getPos().z = rpos.z + parentJoint.getPos().z;

                // Concatenate rotations
                Quaternion asdf = new Quaternion(parentJoint.getOrient());
                asdf.multLocal(thisJoint.getOrient());
                asdf.normalize();
//                thisJoint.setOrient(asdf);
//                thisJoint.setParent(parent);
            }
            jointsNew[i] = thisJoint;
            positions[i] = new Vector3f(jointsNew[i].getPos());
            orientations[i] = new Quaternion(jointsNew[i].getOrient());

            newFrame.setJoint(thisJoint, i);
            newFrame.setPositions(positions);
            newFrame.setRotations(orientations);
        }

        return newFrame;
    }

    private MD5Frame interpolate(MD5Frame lastFrame, MD5Frame nextFrame, float percentage) {
        interpolatedFrame.setJoints(new MD5Joint[this.getNumJoints()]);
        for (int i = 0; i < lastFrame.getPositions().length; i++) {
            MD5Joint newJoint = new MD5Joint();
            newJoint.setPos(new Vector3f(
                    lastFrame.getPositions()[i].x + percentage * (nextFrame.getPositions()[i].x - lastFrame.getPositions()[i].x),
                    lastFrame.getPositions()[i].y + percentage * (nextFrame.getPositions()[i].y - lastFrame.getPositions()[i].y),
                    lastFrame.getPositions()[i].z + percentage * (nextFrame.getPositions()[i].z - lastFrame.getPositions()[i].z)));

            newJoint.setOrient(new Quaternion(
                    lastFrame.getRotations()[i].x + percentage * (nextFrame.getRotations()[i].x - lastFrame.getRotations()[i].x),
                    lastFrame.getRotations()[i].y + percentage * (nextFrame.getRotations()[i].y - lastFrame.getRotations()[i].y),
                    lastFrame.getRotations()[i].z + percentage * (nextFrame.getRotations()[i].z - lastFrame.getRotations()[i].z),
                    lastFrame.getRotations()[i].w + percentage * (nextFrame.getRotations()[i].w - lastFrame.getRotations()[i].w)));

            newJoint.setOrient(lastFrame.getRotations()[i]);
            interpolatedFrame.setJoint(newJoint, i);
        }
        return interpolatedFrame;
    }
}
