package org.usfirst.team3132.frc2016.subsystems;

import org.usfirst.team3132.frc2016.GlobalSingleton;
import org.usfirst.team3132.lib.Loopable;
import org.usfirst.team3132.lib.Subsystem;
import org.usfirst.team3132.lib.Test;
import org.usfirst.team3132.lib.util.MathUtil;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class Drivebase extends Subsystem implements Loopable, Test{
	// global content
	GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
	
	// setup for singleton
	private static Drivebase ourInstance = new Drivebase();
	
	public static Drivebase getInstance() {
		return ourInstance;
	}

	// set up all motors
	SpeedController driveLeftMaster = new VictorSP(7);
	SpeedController driveLeftSlave = new VictorSP(8);
	SpeedController driveLeftSlaveInverted = new VictorSP(9);
	
	SpeedController driveRightMaster = new VictorSP(4);
	SpeedController driveRightSlave = new VictorSP(5);
	SpeedController driveRightSlaveInverted = new VictorSP(6);
	
	// shifter
	DoubleSolenoid shifter = new DoubleSolenoid(0,1);
	
	// control and use variables
	double throttleLeft = 0.0;
	double throttleRight = 0.0;
	double prevThrottleLeft = 0.0;
	double prevThrottleRight = 0.0;
	double tempThrottleLeft = 0.0;
	double tempThrottleRight = 0.0;
	final double maxPercentChangePerLoop = 0.2;
	
	
	private Drivebase() {
		super("drivebase");
		
		// set up motors
		driveLeftMaster.setInverted(true);
		driveLeftSlave.setInverted(false);
		driveLeftSlaveInverted.setInverted(false);
		
		driveRightMaster.setInverted(true);
		driveRightSlave.setInverted(false);
		driveRightSlaveInverted.setInverted(true);
		
		setLeft(0.0);
		setRight(0.0);

		// solenoid
		shiftLowGear();
		enable();
	}

	//////////////////////////////////////////////////////////////////////////////////
	// Operation
	//////////////////////////////////////////////////////////////////////////////////
	

	@Override
	public void update() {
		if(!enabled){
			setLeft(0.0);
			setRight(0.0);
			return;
		}
		
		if(globalSingleton.drivebaseAllowedToDrive){
			double leftDiff = throttleLeft - prevThrottleLeft;
			double rightDiff = throttleRight - prevThrottleRight;
			
			if(Math.abs(leftDiff) > maxPercentChangePerLoop)
				tempThrottleLeft += maxPercentChangePerLoop * Math.signum(leftDiff);
			else
				tempThrottleLeft = throttleLeft;
			
			if(Math.abs(rightDiff) > maxPercentChangePerLoop)
				tempThrottleRight += maxPercentChangePerLoop * Math.signum(rightDiff);
			else
				tempThrottleRight = throttleRight;
			
			prevThrottleLeft = throttleLeft;
			prevThrottleRight = throttleRight;
			
			// set motors
			if(globalSingleton.drivebaseVRamping){
				setLeft(tempThrottleLeft);
				setRight(tempThrottleRight);
			} else {
				setLeft(throttleLeft);
				setRight(throttleRight);
			}
		} else {
			setLeft(0.0);
			setRight(0.0);
		}
		
		//System.out.println("throttle Left: " + throttleLeft + " throttle Right: " + throttleRight);
		//System.out.println("drivebase updated");
	}

	//////////////////////////////////////////////////////////////////////////////////
	// Test Mode
	//////////////////////////////////////////////////////////////////////////////////

	protected int testState = 0;
	protected int testCounter = 0;
	
	public void testInit() {
		testState = 0;
		testCounter = 0;
	}
	
	public void testPeriodic() {
		switch(testState){
			case 0:
				driveLeftMaster.set(0.5);
				System.out.println("leftMaster");
				break;
			case 1:
				driveLeftSlave.set(0.5);
				System.out.println("leftSlave");
				break;
			case 2:
				driveLeftSlaveInverted.set(0.5);
				System.out.println("leftSlaveInverted");
				break;
			case 3:
				driveRightMaster.set(0.5);
				System.out.println("rightMaster");
				break;
			case 4:
				driveRightSlave.set(0.5);
				System.out.println("rightSlave");
				break;
			case 5:
				driveRightSlaveInverted.set(0.5);
				System.out.println("rightSlaveInverted");
				break;
			default:
				System.out.println("no motors active, reseting");
		}
		
		System.out.println("state: " + testState);
		testCounter++;
		if(testCounter > 200){
			testCounter = 0;
			testState++;
			setLeft(0.0);
			setRight(0.0);
			if(testState > 7){
				testState = 0;
			}
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////
	// Interfacing
	//////////////////////////////////////////////////////////////////////////////////

	double oldTurn = 0.0;
	public void driveWheel(double move, double turn, boolean quickTurn){
		double left = 0;
        double right = 0;
        double leftOverpower = 0;
        double rightOverpower = 0;
        double overPowerGain = 0.1;
        boolean useOverpower = false;

        // calculate negative inertia to help robot anticipate driver actions
        //double negIntertia = turn - oldTurn;
        oldTurn = turn;

        if(quickTurn){
            left = move + turn;
            right = move - turn;
        } else {
            left = move + Math.abs(move)*turn;
            right = move - Math.abs(move)*turn;
        }

        if(!useOverpower){
            setLeftThrottle(left);
            setRightThrottle(right);
            return;
        }

        if(left > 1){
            leftOverpower = left - 1;
            left = 1;
        }else if(left < -1){
            leftOverpower = left + 1;
            left = -1;
        }

        if(right > 1){
            rightOverpower = right - 1;
            right = 1;
        } else if(right < -1){
            rightOverpower = right + 1;
            right = -1;
        }

        left = left - rightOverpower*overPowerGain;
        right = right - leftOverpower*overPowerGain;

        setLeftThrottle(left);
        setRightThrottle(right);
	}
	
	public void setLeftThrottle(double speed){
		throttleLeft = MathUtil.limitValue(speed);
	}
	
	public void setRightThrottle(double speed){
		throttleRight = MathUtil.limitValue(speed);
	}
	
	public void setThrottle(double left, double right){
		setLeftThrottle(left);
		setRightThrottle(right);
	}
	
	protected void setLeft(double speed){
		driveLeftMaster.set(speed);
		driveLeftSlave.set(speed);
		driveLeftSlaveInverted.set(speed);
	}
	
	protected void setRight(double speed){
		driveRightMaster.set(speed);
		driveRightSlave.set(speed);
		driveRightSlaveInverted.set(speed);
	}
	
	public void shiftHighGear(){
		shifter.set(Value.kReverse);
	}
	
	public void shiftLowGear(){
		shifter.set(Value.kForward);
	}

}
