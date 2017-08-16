package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.lib.LogitechGamepadF310;
import org.usfirst.team3132.lib.Test;

import edu.wpi.first.wpilibj.Timer;

public class DriveOutputTest implements Test{

	LogitechGamepadF310 gamepad = new LogitechGamepadF310(0);
	Timer t = new Timer();
	double leftOut = 0.0;
	double rightOut = 0.0;
	
	@Override
	public void testInit() {
		t.reset();
		t.start();
		
	}

	@Override
	public void testPeriodic() {
		boolean quickturn = gamepad.getLeftStickClick()|| gamepad.getRightStickClick() || gamepad.getLeftXScaled() == 0;
		driveWheel(gamepad.getLeftYScaled(), gamepad.getRightXScaled(), quickturn);
		
		System.out.println("t: 	" + t.get() + 
				"	move:	" + gamepad.getLeftYScaled() + 
				"	wheel:	" + gamepad.getRightXScaled() + 
				"	quickturn:	" + quickturn +
				"	negIntertia:	" + negIntertia +
				"	leftOut:	" + leftOut + 
				"	rightOut:	" + rightOut);
	}

	
	double negIntertia = 0.0;
	double oldTurn = 0.0;
	public void driveWheel(double move, double turn, boolean quickTurn){
		double left = 0;
        double right = 0;
        double leftOverpower = 0;
        double rightOverpower = 0;
        double overPowerGain = 0.1;
        boolean useOverpower = false;

        // calculate negative inertia to help robot anticipate driver actions
        negIntertia = turn - oldTurn;
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
	
	public void setLeftThrottle(double left){
		leftOut = left;
	}
	
	public void setRightThrottle(double right){
		rightOut = right;
	}
}
