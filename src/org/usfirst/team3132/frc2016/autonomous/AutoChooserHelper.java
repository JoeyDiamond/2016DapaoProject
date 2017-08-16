package org.usfirst.team3132.frc2016.autonomous;

import org.usfirst.team3132.frc2016.Constants;
import org.usfirst.team3132.lib.ChooserHelper;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooserHelper implements ChooserHelper{
	SendableChooser<String> autoChooser = new SendableChooser<String>();;

	
	@Override
	public void create() {
		autoChooser.addDefault("Default Auto", Constants.autoDefaultEmpty);
        autoChooser.addObject("stationary auto", Constants.autoStationary);
        autoChooser.addObject("reversing auto", Constants.autoReversing);
        SmartDashboard.putData("Auto choices", autoChooser);
		
	}

	@Override
	public String getSelection() {
		return (String) autoChooser.getSelected();
	}
	
}
