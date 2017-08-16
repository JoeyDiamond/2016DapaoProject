package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.lib.LogitechGamepadF310;
import org.usfirst.team3132.lib.Test;
import org.usfirst.team3132.lib.util.MathUtil;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;

public class TonguePIDTest implements Test {

	CANTalon tongue = new CANTalon(5);
	
	LogitechGamepadF310 stick = new LogitechGamepadF310(0);
	Timer t = new Timer();
	
	final double tongueTolerance = 1;
	final int tongueToleranceCount = 5;
	int tongueToleranceCounter = 0;
	
	final int tongueLimitDownRaw = 850;
	final int tongueLimitUpRaw = 250;
	final double tongueLimitDownDeg = -80;
	final double tongueLimitUpDeg = 90;
	
	public TonguePIDTest() {
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
	}
	
	double angleSet = 0;
	
	@Override
	public void testInit() {
		System.out.flush();
		t.reset();
		t.start();
		angleSet = 45;
	}

	@Override
	public void testPeriodic() {
		if(stick.getGreenButton()){
			angleSet = 0;
		} else if(stick.getRedButton()){
			angleSet = 30;
		} else if(stick.getYellowButton()){
			angleSet = 45;
		} else if(stick.getStartButton()){
			angleSet = this.tongueLimitUpDeg;
		} else if(stick.getBackButton()){
			angleSet = this.tongueLimitDownDeg;
		}
		
		setTongueDeg(angleSet);
		System.out.println(t.get() + "	" + getTongueTargetDeg() + "	" + getTongueCurrDeg());
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
		return MathUtil.scale(tongue.get(), this.tongueLimitDownRaw, this.tongueLimitUpRaw,
				this.tongueLimitDownDeg, this.tongueLimitUpDeg);
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
	

}
