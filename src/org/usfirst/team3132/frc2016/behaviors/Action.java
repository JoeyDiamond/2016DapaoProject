package org.usfirst.team3132.frc2016.behaviors;

import org.usfirst.team3132.frc2016.GlobalSingleton;
import org.usfirst.team3132.frc2016.subsystems.Climber;
import org.usfirst.team3132.frc2016.subsystems.Drivebase;
import org.usfirst.team3132.frc2016.subsystems.Shooter;

public abstract class Action implements Runnable{
	GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
	
	Drivebase drivebase = Drivebase.getInstance();
	Shooter shooter = Shooter.getInstance();
	Climber climber = Climber.getInstance();
	
}
