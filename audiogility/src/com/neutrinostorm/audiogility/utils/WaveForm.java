package com.neutrinostorm.audiogility.utils;

public class WaveForm {
	private int frequency;
	private int amplitude;
	private WaveType waveType;
	
	private int top;
	private int left;
	private int width;
	private int height;
	private int centerLine;
	
	public WaveForm(int top, int left, int width, int height) {
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;	
		this.centerLine = top - (height / 2);
	}
	
	public WaveForm(int frequency, int amplitude, WaveType waveType, int top, int left, int width, int height)
	{
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.waveType = waveType;
		
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.centerLine = top - (height / 2);
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public int getAmplitude() {
		return this.amplitude;
	}
	
	public void setAmplitude(int amplitude) {
		this.amplitude = amplitude;
	}
	

	public WaveType getWaveType() {
		return this.waveType;
	}
	
	public void setWaveType(WaveType waveType) {
		this.waveType = waveType;
	}

	public int getTop() {
		return this.top;
	}
	
	public int getLeft() {
		return this.left;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getCenterLine() {
		return this.centerLine;
	}
}
