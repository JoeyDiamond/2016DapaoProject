package org.usfirst.team3132.frc2016.autonomous;

import org.usfirst.team3132.frc2016.behaviors.Shoot3Shots;
import org.usfirst.team3132.lib.util.SystemUtil;

import edu.wpi.first.wpilibj.Timer;

public class ReversingShootingCenterBack extends AutonomousRoutine{

	Thread thread;
	Timer t = new Timer();
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		System.out.println("reversing shooting auto started");

		thread = new Thread(new Shoot3Shots());
		thread.start();
		while(ds.isAutonomous() && thread.isAlive()){
			SystemUtil.slowLoopDelay();
		}
		
		t.reset();
		t.start();
		drivebase.setThrottle(-0.5, -0.5);
		while(t.get()< 3 && ds.isAutonomous()){
			SystemUtil.fastLoopDelay();
		}
		drivebase.setThrottle(0.0, 0.0);
		t.stop();
		System.out.println("reversing shooting auto ended");
		
	}
	
}
