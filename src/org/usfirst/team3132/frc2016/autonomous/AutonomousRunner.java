package org.usfirst.team3132.frc2016.autonomous;

public class AutonomousRunner {
	private AutonomousRoutine routine = new EmptyAutonomous();
	private Thread thread;
	
	public void setAutoRoutine(AutonomousRoutine newRoutine){
		routine = newRoutine;
	}
	
	public void start(){
		thread = new Thread(routine);
		thread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void kill(){
		if(thread != null){
			try{
				thread.stop();
				System.out.println("auto routine killed");
			} catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	public boolean isRunning(){
		return thread.isAlive();
	}
}
