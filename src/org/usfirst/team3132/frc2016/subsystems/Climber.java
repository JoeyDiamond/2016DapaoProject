package org.usfirst.team3132.frc2016.subsystems;

import org.usfirst.team3132.frc2016.GlobalSingleton;
import org.usfirst.team3132.lib.Subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Climber extends Subsystem{

	GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
	
	// setup for singleton
	private static Climber ourInstance = new Climber();
	
	public static Climber getInstance() {
		return ourInstance;
	}

	private Climber() {
		super("climber");
	}

	
	// config
	DoubleSolenoid climber = new DoubleSolenoid(2,3);
	
	public Climber(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void extend(){
		climber.set(Value.kReverse);
	}
	
	public void retract(){
		climber.set(Value.kForward);
	}

	
}
