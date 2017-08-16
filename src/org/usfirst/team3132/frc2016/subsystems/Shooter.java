/* Shooter.java Changelog
 * 
 * - 2016-09-19 Joey: code review with luan
 * - 2016-09-17 Joey: file creation
 */


package org.usfirst.team3132.frc2016.subsystems;

import org.usfirst.team3132.frc2016.GlobalSingleton;
import org.usfirst.team3132.lib.Loopable;
import org.usfirst.team3132.lib.Subsystem;
import org.usfirst.team3132.lib.util.MathUtil;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem implements Loopable{

	GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
	
	// setup for singleton
	private static Shooter ourInstance = new Shooter();
	
	public static Shooter getInstance() {
		return ourInstance;
	}
	
	// motors and solenoids
	CANTalon flywheelMaster = new CANTalon(6);
	CANTalon flywheelSlave = new CANTalon(7);
	CANTalon tongue = new CANTalon(5);
	DoubleSolenoid kicker = new DoubleSolenoid(4,5);
	
	final double flywheelTolerance = 50;
	final int flywheelToleranceCount = 5;
	int flywheelToleranceCounter = 0;
	
	final double tongueTolerance = 1;
	final int tongueToleranceCount = 5;
	int tongueToleranceCounter = 0;
	
	final int tongueLimitDownRaw = 850;
	final int tongueLimitUpRaw = 250;
	final double tongueLimitDownDeg = -80;
	final double tongueLimitUpDeg = 90;
	
	
	
	private Shooter() {
		super("shooter");
		
		// flywheel slave setup
		flywheelSlave.changeControlMode(TalonControlMode.Follower);
		flywheelSlave.enableBrakeMode(false);
		flywheelSlave.set(6);
		
		// flywheel master setup
		flywheelMaster.changeControlMode(TalonControlMode.Speed);
		flywheelMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		flywheelMaster.enableBrakeMode(false);
		flywheelMaster.configEncoderCodesPerRev(360);
		flywheelMaster.reverseSensor(true);
		flywheelMaster.reverseOutput(true);
		
		flywheelMaster.setPID(1.9, 0.00000001, 50);//0.000001
		flywheelMaster.setIZone(1);
		flywheelMaster.setF(0.1);
		flywheelMaster.configMaxOutputVoltage(12.0);
    	flywheelMaster.configNominalOutputVoltage(0.0, 0.0);
		
    	// tongue setup
    	tongue.clearStickyFaults();
		tongue.changeControlMode(TalonControlMode.Position);
		tongue.setFeedbackDevice(FeedbackDevice.AnalogPot);
		tongue.reverseSensor(false);
		tongue.reverseOutput(false);
		tongue.setF(0.0);
		tongue.setPID(20, 0.1, 0.0);
		tongue.setIZone(100);
		tongue.clearStickyFaults();
		tongue.enableForwardSoftLimit(true);
		tongue.enableReverseSoftLimit(true);
		tongue.setForwardSoftLimit(tongueLimitDownRaw);
		tongue.setReverseSoftLimit(tongueLimitUpRaw);
		
		// kicker setup
		kickerRetract();
		
		setTongueDeg(getTongueCurrDeg());
		
		// smart dashboard setup
		SmartDashboard.putNumber("flywheel target RPM", getFlywheelTargetRPM());
		SmartDashboard.putNumber("flywheel curr RPM", getFlywheelCurrentRPM());
		SmartDashboard.putNumber("flywheel F", flywheelMaster.getF());
		SmartDashboard.putNumber("flywheel P", flywheelMaster.getP());
		SmartDashboard.putNumber("flywheel I", flywheelMaster.getI());
		SmartDashboard.putNumber("flywheel D", flywheelMaster.getD());
		
		SmartDashboard.putNumber("tongue target", getTongueTargetDeg());
		SmartDashboard.putNumber("tongue curr Deg", getTongueCurrDeg());
		SmartDashboard.putNumber("tongue F", tongue.getF());
		SmartDashboard.putNumber("tongue P", tongue.getP());
		SmartDashboard.putNumber("tongue I", tongue.getI());
		SmartDashboard.putNumber("tongue D", tongue.getD());
		
		SmartDashboard.putBoolean("kicker in", getKickerIn());  
	}

	//////////////////////////////////////////////////////////////////////////////////
	// Operation
	//////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void update(){
		// smart dashboard
		SmartDashboard.putNumber("flywheel target RPM", getFlywheelTargetRPM());
		SmartDashboard.putNumber("flywheel curr RPM", getFlywheelCurrentRPM());
		SmartDashboard.putNumber("flywheel F", flywheelMaster.getF());
		SmartDashboard.putNumber("flywheel P", flywheelMaster.getP());
		SmartDashboard.putNumber("flywheel I", flywheelMaster.getI());
		SmartDashboard.putNumber("flywheel D", flywheelMaster.getD());
		
		SmartDashboard.putNumber("tongue target", getTongueTargetDeg());
		SmartDashboard.putNumber("tongue curr Deg", getTongueCurrDeg());
		SmartDashboard.putNumber("tongue F", tongue.getF());
		SmartDashboard.putNumber("tongue P", tongue.getP());
		SmartDashboard.putNumber("tongue I", tongue.getI());
		SmartDashboard.putNumber("tongue D", tongue.getD());
		
		SmartDashboard.putBoolean("kicker target extended", getKickerIn()); 
		
		//setTongueDeg(globalSingleton.shooterAngleTarget);
		//setFlywheelTargetRPM(globalSingleton.shooterRPMTarget);
		
		globalSingleton.shooterTongueInPosition = isTongueInPosition();
		globalSingleton.shooterFlywheelAtSpeed = isFlywheelAtSpeed();
		globalSingleton.shooterIsReady = isReadyToShoot();
		
		//System.out.println("shooter updated");
		//System.out.println("tongue target: " + getTongueCurrDeg() + " tongue current: " + getTongueTargetDeg());
	}
	
	public boolean isReadyToShoot(){
		return isFlywheelAtSpeed() && isTongueInPosition();
	}


	//////////////////////////////////////////////////////////////////////////////////
	// Test mode
	//////////////////////////////////////////////////////////////////////////////////

	protected String testKey = "flywheel";

	public void test(){
		switch(testKey){
			case "flywheel":
				testFlywheel();
				break;
			case "tongue":
				testTongue();
				break;
			case "kicker":
				testKicker();
				break;
			default:
				setFlywheelTargetRPM(0.0);
				//setTongueDeg(0.0);
				break;
		}
	}
	
	public void testMode(String key){
		testKey = key;
	}

	public void testFlywheel() {
		double target = SmartDashboard.getNumber("flywheel Target RPM", 0.0);
		double f = SmartDashboard.getNumber("flywheel F", 0.0);
		double p = SmartDashboard.getNumber("flywheel P", 0.0);
		double i = SmartDashboard.getNumber("flywheel I", 0.0);
		double d = SmartDashboard.getNumber("flywheel D", 0.0);
		setFlywheelFPID(f,p,i,d);
		setFlywheelTargetRPM(target);
	}
	
	public void testTongue(){
		double target = SmartDashboard.getNumber("tongue Target", 0.0);
		double f = SmartDashboard.getNumber("tongue F", 0.0);
		double p = SmartDashboard.getNumber("tongue P", 0.0);
		double i = SmartDashboard.getNumber("tongue I", 0.0);
		double d = SmartDashboard.getNumber("tongue D", 0.0);
		setTongueFPID(f,p,i,d);
		setTongueDeg(target);
	}
	
	public void testKicker(){
		if(SmartDashboard.getBoolean("kicker target extended", false)){
			kickerExtend();
		} else {
			kickerRetract();
		}
	}
	//////////////////////////////////////////////////////////////////////////////////
	// flywheel control
	//////////////////////////////////////////////////////////////////////////////////
	
	public void setFlywheelTargetRPM(double speed){
		if(speed == 0){
			flywheelMaster.changeControlMode(TalonControlMode.PercentVbus);
			flywheelMaster.set(0.0);
			return;
		} else {
			flywheelMaster.changeControlMode(TalonControlMode.Speed);
			flywheelMaster.set(speed);// * 60.0);
		}
		
	}

	public boolean isFlywheelAtSpeed(){
		if(Math.abs(getFlywheelTargetRPM() - getFlywheelCurrentRPM()) < flywheelTolerance){
			flywheelToleranceCounter++;
		} else
			flywheelToleranceCounter = 0;
		
		return flywheelToleranceCounter >= flywheelToleranceCount;
	}
	
	public double getFlywheelTargetRPM(){
		return flywheelMaster.getSetpoint();// / 60;
	}
	
	public double getFlywheelCurrentRPM(){
		return flywheelMaster.getSpeed();// / 60;
	}
	
	public void setFlywheelFPID(double f, double p, double i, double d){
		flywheelMaster.setF(f);
		flywheelMaster.setPID(p, i, d);
	}
	
	public double getFlywheelF(){
		return flywheelMaster.getF();
	}
	
	public double getFlywheelP(){
		return flywheelMaster.getP();
	}
	
	public double getFlywheelI(){
		return flywheelMaster.getI();
	}
	
	public double getFlywheelD(){
		return flywheelMaster.getD();
	}
	

	//////////////////////////////////////////////////////////////////////////////////
	// tongue control
	//////////////////////////////////////////////////////////////////////////////////
	
	public void setTongueToStorage(){
		setTongueDeg(85);
	}
	
	public void setTonguePos(int pos){
		tongue.set(pos);
	}
	
	public boolean isTongueInPosition(){
		if(Math.abs(getTongueTargetDeg() - getTongueCurrDeg()) < tongueTolerance){
			tongueToleranceCounter++;
		} else {
			tongueToleranceCounter = 0;
		}
		return tongueToleranceCounter >= tongueToleranceCount;
	}
	
	public void setTongueDeg(double pos){
		tongue.set(MathUtil.scale(pos, this.tongueLimitDownDeg,this.tongueLimitUpDeg,
				this.tongueLimitDownRaw,this.tongueLimitUpRaw));
	}
	
	public double getTongueTargetDeg(){
		return MathUtil.scale(tongue.getSetpoint(), this.tongueLimitDownRaw,this.tongueLimitUpRaw,
				this.tongueLimitDownDeg,this.tongueLimitUpDeg);
	}
	
	public double getTongueCurrDeg(){
		return MathUtil.scale(tongue.getPosition(), this.tongueLimitDownRaw, this.tongueLimitUpRaw,
				this.tongueLimitDownDeg, this.tongueLimitUpDeg);
	}
	
	public double getTongueCurrRaw(){
		return tongue.getPosition();
	}
	
	public void setTongueFPID(double f, double p, double i, double d){
		tongue.setF(f);
		tongue.setPID(p, i, d);
	}
	
	public double getTongueF(){
		return tongue.getF();
	}
	
	public double getTongueP(){
		return tongue.getP();
	}
	
	public double getTongueI(){
		return tongue.getI();
	}
	
	public double getTongueD(){
		return tongue.getD();
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////
	// feeder control
	//////////////////////////////////////////////////////////////////////////////////
	
	public void kickerExtend(){
		kicker.set(Value.kForward);
	}
	
	public void kickerRetract(){
		if(this.getTongueCurrDeg() < 60)
			kicker.set(Value.kReverse);
	}
	
	public boolean getKickerIn(){
		return kicker.get() == Value.kForward;
	}
}
