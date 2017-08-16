package org.usfirst.team3132.frc2016;

public class GlobalSingleton {
	
	// drivebase
	public boolean autoDrive = false;
	public boolean drivebaseAllowedToDrive = true;
	public boolean drivebaseVRamping = true;
	
	// shooter
	public boolean shooterAutomated = false;
	public double shooterRPMTarget = 0;
	public double shooterRPMCurrent = 0;
	public double shooterAngleTarget = 0;
	public double shotoerAngleCurrent = 0;
	public boolean shooterIsReady = false;
	public boolean shooterTongueInPosition = false;
	public boolean shooterFlywheelAtSpeed = false;
	
	// climber
	public boolean climberExtended = false;
	
	
	
	// setup for singleton
	private static GlobalSingleton ourInstance = new GlobalSingleton();
	
	public static GlobalSingleton getInstance() {
		return ourInstance;
	}

	private GlobalSingleton() {
		
	}
	
	
}
