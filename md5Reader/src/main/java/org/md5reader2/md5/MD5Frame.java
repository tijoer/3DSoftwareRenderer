/**
 * 
 */
package org.md5reader2.md5;

import org.util.Quaternion;
import org.util.Vector3f;

/**
 * @author Marco Frisan, Tim Joergen
 * 
 * I try an implementation of a MD5 frame handler. This is done in a per frame
 * (like in MD5 format) architecture. Each MD5Frame, contains a ordered array of
 * transformations. Each transformation corresponds to the joint positioned at
 * the same index.
 * 
 */
public class MD5Frame {

	// The Vector containing transformations.
	private Vector3f[] positions;
	private Quaternion[] rotations;
    private MD5Joint[] joints;
    private long frameStartTime;
    
	/**
	 * 
	 */
	public MD5Frame() {
		super();
	}

	/**
	 * @return the positions
	 */
	public Vector3f[] getPositions() {
		return positions;
	}

	/**
	 * @param positions the positions to set
	 */
	public void setPositions(Vector3f[] positions) {
		this.positions = positions;
	}

	/**
	 * @return the rotations
	 */
	public Quaternion[] getRotations() {
		return rotations;
	}

	/**
	 * @param rotations the rotations to set
	 */
	public void setRotations(Quaternion[] rotations) {
		this.rotations = rotations;
	}
	
    public MD5Joint[] getJoints() {
        return this.joints;
    }
    
    public void setJoints(MD5Joint[] joints) {
        this.joints = joints;
    }

    void setJoint(MD5Joint joint, int positionInJointsArray) {
        this.joints[positionInJointsArray] = joint;
    }

    public long getFrameStartTime() {
        return frameStartTime;
    }

    public void setFrameStartTime(long frameStartTime) {
        this.frameStartTime = frameStartTime;
    }
}
