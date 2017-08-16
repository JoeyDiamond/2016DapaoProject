package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.frc2016.Constants;
import org.usfirst.team3132.lib.ChooserHelper;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TestChooserHelper implements ChooserHelper{
	// chooser
	SendableChooser<String> testChooser = new SendableChooser<String>();
    
	@Override
	public void create(){
		testChooser.addDefault(Constants.testDrivebase, Constants.testDrivebase);
		testChooser.addObject(Constants.testFlywheel, Constants.testFlywheel);
		testChooser.addObject(Constants.testTongue, Constants.testTongue);
		testChooser.addObject(Constants.testKicker, Constants.testKicker);
		testChooser.addObject(Constants.testClimber, Constants.testClimber);
		testChooser.addObject(Constants.testJoyScaling, Constants.testJoyScaling);
		testChooser.addObject(Constants.testDriveMath, Constants.testDriveMath);
		SmartDashboard.putData("Test Selection", testChooser);
	}

	@Override
	public String getSelection() {
		return (String) testChooser.getSelected();
	}
}
