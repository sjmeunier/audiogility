package com.neutrinostorm.audiogility.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.neutrinostorm.audiogility.Audiogility;
import com.neutrinostorm.audiogility.services.AudiogilityMusic;
import com.neutrinostorm.audiogility.services.AudiogilitySound;
import com.neutrinostorm.audiogility.utils.Level;
import com.neutrinostorm.audiogility.utils.LevelLoader;
import com.neutrinostorm.audiogility.utils.Rand;
import com.neutrinostorm.audiogility.utils.SoundMapping;
import com.neutrinostorm.audiogility.utils.StopWatch;
import com.neutrinostorm.audiogility.utils.WaveForm;
import com.neutrinostorm.audiogility.utils.WaveType;
 
public class GameScreen extends AbstractScreen
{
    private Image backgroundImage;
    private Image powerButtonImage;
    
    
    private WaveForm[] waveForms = new WaveForm[4];
    private WaveForm[] targetWaveForms = new WaveForm[4];
    private Level level;
    private float WAVESCALE = 0.0625f;
    private float OUTPUTSCALE = 300;
    
    private Rand rand = new Rand();
    
    private ShapeRenderer waveRenderer = new ShapeRenderer();
    
    private AudioDevice audioDevice;
    private int SAMPLE_RATE = 44100;
    private int NUM_SAMPLES = 22050;
    private short[] soundPCM = new short[NUM_SAMPLES];
    
    private int OUTPUT_WIDTH = 370;
    private int OUTPUT_HEIGHT = 200;
    private int OUTPUT_LEFT = 410;
    private int OUTPUT_TOP = 420;
    
    private short MAX_AMPLITUDE = 32767;
    private short MIN_AMPLITUDE = -32766;
    private float TWOPI = (float)Math.PI * 2.0f;
    private Thread audioThread;
    private boolean audioRunning = false;
    private boolean levelComplete = false;
    
    private StopWatch stopWatch;
    private Label timeLabel;
    
    public GameScreen(Audiogility game, int levelId)
    {
        super(game);
        
        this.level = LevelLoader.LoadLevel(levelId);
        this.targetWaveForms = this.level.getWaveForms();
        waveForms[0] = new WaveForm(rand.getInt(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, this.targetWaveForms[0].getFrequency()), rand.getInt(5, SoundMapping.MAX_AMPLITUDE, this.targetWaveForms[0].getAmplitude()), WaveType.Sine, 420, 20, 176, 90);
        if (this.level.getWaveCount() > 1)
        	waveForms[1] = new WaveForm(rand.getInt(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, this.targetWaveForms[0].getFrequency()), rand.getInt(5, SoundMapping.MAX_AMPLITUDE, this.targetWaveForms[0].getAmplitude()), WaveType.Sine, 420, 208, 176, 90);
        else
        	waveForms[1] = new WaveForm(0, 0, WaveType.Sine, 420, 208, 176, 90);
        	
        if (this.level.getWaveCount() > 2)
        	waveForms[2] = new WaveForm(rand.getInt(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, this.targetWaveForms[0].getFrequency()), rand.getInt(5, SoundMapping.MAX_AMPLITUDE, this.targetWaveForms[0].getAmplitude()), WaveType.Sine, 310, 20, 176, 90);
        else
        	waveForms[2] = new WaveForm(0, 0, WaveType.Sine, 310, 20, 176, 90);

        if (this.level.getWaveCount() > 3)
        	waveForms[3] = new WaveForm(rand.getInt(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, this.targetWaveForms[0].getFrequency()), rand.getInt(5, SoundMapping.MAX_AMPLITUDE, this.targetWaveForms[0].getAmplitude()), WaveType.Sine, 310, 208, 176, 90);
        else
        	waveForms[3] = new WaveForm(0, 0, WaveType.Sine, 310, 208, 176, 90);


        waveRenderer = new ShapeRenderer();
        waveRenderer.setProjectionMatrix(camera.combined);
        audioDevice = Gdx.audio.newAudioDevice(SAMPLE_RATE, true);
        audioDevice.setVolume(0);
        //Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
        
        audioRunning = true;
        updateSound();
        // start a new thread to synthesise audio
        audioThread = new Thread() {
        	boolean keepRunning;
        	
        	public void run() {
        		// set process priority
        		setPriority(Thread.MAX_PRIORITY);
        		// set the buffer size
        		keepRunning = true;
        		while(keepRunning) {
        			if(audioRunning) {
        				try{ 
        					audioDevice.writeSamples(soundPCM, 0, NUM_SAMPLES);
        				} catch(Exception e) {
        					audioRunning = false;
        					keepRunning = false;
        				}
        			} else {
        				try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
        			}
        		}

        	}
        };
		audioThread.start();    
		stopWatch = new StopWatch();
		stopWatch.start();
    }
 
    private void updateSound() {
		float rate1 = (float)this.SAMPLE_RATE / (float)SoundMapping.Frequencies[this.waveForms[0].getFrequency()];
		float rate2 = (float)this.SAMPLE_RATE / (float)SoundMapping.Frequencies[this.waveForms[1].getFrequency()];
		float rate3 = (float)this.SAMPLE_RATE / (float)SoundMapping.Frequencies[this.waveForms[2].getFrequency()];
		float rate4 = (float)this.SAMPLE_RATE / (float)SoundMapping.Frequencies[this.waveForms[3].getFrequency()];
		
		float amplitude1 = (float)this.waveForms[0].getAmplitude();
		float amplitude2 = (float)this.waveForms[1].getAmplitude();
		float amplitude3 = (float)this.waveForms[2].getAmplitude();
		float amplitude4 = (float)this.waveForms[3].getAmplitude();
		
		double val;
		
        for (int i = 0; i < this.NUM_SAMPLES; i++) {
        	val = ((Math.sin(this.TWOPI * (float)i / rate1) * amplitude1) 
        			+ (Math.sin(this.TWOPI * (float)i / rate2) * amplitude2)
        			+ (Math.sin(this.TWOPI * (float)i / rate3) * amplitude3)
        			+ (Math.sin(this.TWOPI * (float)i / rate4) * amplitude4)) * this.OUTPUTSCALE;
        	if (val > 0)
        		this.soundPCM[i] = (short) Math.min((short)val, this.MAX_AMPLITUDE);
        	else
        		this.soundPCM[i] = (short) Math.max((short)val, this.MIN_AMPLITUDE);
        }
    }
    
    @Override
    public void show()
    {
        super.show();
        
        game.getMusicManager().play( AudiogilityMusic.LEVEL );
 
        //Set up background
        AtlasRegion backgroundRegion = game.getAtlas().findRegion( "game-screen/background" );
        Drawable backgroundDrawable = new TextureRegionDrawable( backgroundRegion );
        backgroundImage = new Image( backgroundDrawable, Scaling.stretch );
        backgroundImage.setFillParent( true );
        backgroundImage.getColor().a = 0f;
        backgroundImage.addAction( sequence( fadeIn( 0.75f )) );
        stage.addActor( backgroundImage );

        //Set up power button
        AtlasRegion powerButtonRegion = game.getAtlas().findRegion( "game-screen/powerbutton" );
        Drawable powerButtonDrawable = new TextureRegionDrawable( powerButtonRegion );

        powerButtonImage = new Image( powerButtonDrawable, Scaling.stretch );
        powerButtonImage.setPosition(VIEWPORT_WIDTH - 50,  VIEWPORT_HEIGHT - 50);
        powerButtonImage.setSize(40, 40);
        
        powerButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	audioRunning = false;
            	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
                game.setScreen( new LevelScreen(game) );
            }
        });

        stage.addActor(powerButtonImage);

    	Label nameLabel = new Label(level.getName(), game.getSkin(), "freestyle");
    	nameLabel.setX(70);
    	nameLabel.setY(428);
    	nameLabel.setWidth(150);
    	nameLabel.setAlignment(Align.left);
    	stage.addActor(nameLabel);
    	
        //Set up sliders
    	Slider frequencySlider1 = new Slider(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, 1, true, game.getSkin(), "blue");
    	frequencySlider1.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[0].setFrequency((int)(((Slider)actor).getValue()));
        			//updateSound();
        		}
        	});

    	frequencySlider1.setWidth(45);
    	frequencySlider1.setHeight(180);
    	frequencySlider1.setX(20);
    	frequencySlider1.setY(20);
    	frequencySlider1.setValue(waveForms[0].getFrequency());
    	stage.addActor(frequencySlider1);
    	
    	Slider amplitudeSlider1 = new Slider(SoundMapping.MIN_AMPLITUDE, SoundMapping.MAX_AMPLITUDE, 1, true, game.getSkin(), "blue");
    	amplitudeSlider1.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[0].setAmplitude((int)(((Slider)actor).getValue()));
        			updateSound();
        		}
        	});

    	amplitudeSlider1.setWidth(45);
    	amplitudeSlider1.setHeight(180);
    	amplitudeSlider1.setX(75);
    	amplitudeSlider1.setY(20);
    	amplitudeSlider1.setValue(waveForms[0].getAmplitude());
    	stage.addActor(amplitudeSlider1);
    	
    	Slider frequencySlider2 = new Slider(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, 1, true, game.getSkin(), "red");
    	frequencySlider2.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[1].setFrequency((int)(((Slider)actor).getValue()));
        			updateSound();
        		}
        	});

    	frequencySlider2.setWidth(45);
    	frequencySlider2.setHeight(180);
    	frequencySlider2.setX(170);
    	frequencySlider2.setY(20);
    	frequencySlider2.setValue(waveForms[1].getFrequency());
    	if (this.level.getWaveCount() < 2)
    		frequencySlider2.setDisabled(true);
    	
    	stage.addActor(frequencySlider2);
    	
    	Slider amplitudeSlider2 = new Slider(SoundMapping.MIN_AMPLITUDE, SoundMapping.MAX_AMPLITUDE, 1, true, game.getSkin(), "red");
    	amplitudeSlider2.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[1].setAmplitude((int)(((Slider)actor).getValue()));
        			updateSound();
        		}
        	});

    	amplitudeSlider2.setWidth(45);
    	amplitudeSlider2.setHeight(180);
    	amplitudeSlider2.setX(225);
    	amplitudeSlider2.setY(20);
    	amplitudeSlider2.setValue(waveForms[1].getAmplitude());
    	if (this.level.getWaveCount() < 2)
    		amplitudeSlider2.setDisabled(true);
    	stage.addActor(amplitudeSlider2);
    	
    	Slider frequencySlider3 = new Slider(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, 1, true, game.getSkin(), "green");
    	frequencySlider3.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[2].setFrequency((int)(((Slider)actor).getValue()));
        			updateSound();
        		}
        	});

    	frequencySlider3.setWidth(45);
    	frequencySlider3.setHeight(180);
    	frequencySlider3.setX(320);
    	frequencySlider3.setY(20);
    	frequencySlider3.setValue(waveForms[2].getFrequency());
    	if (this.level.getWaveCount() < 3)
    		frequencySlider3.setDisabled(true);
    	stage.addActor(frequencySlider3);
    	
    	Slider amplitudeSlider3 = new Slider(SoundMapping.MIN_AMPLITUDE, SoundMapping.MAX_AMPLITUDE, 1, true, game.getSkin(), "green");
    	amplitudeSlider3.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[2].setAmplitude((int)(((Slider)actor).getValue()));
        			updateSound();
        		}
        	});

    	amplitudeSlider3.setWidth(45);
    	amplitudeSlider3.setHeight(180);
    	amplitudeSlider3.setX(375);
    	amplitudeSlider3.setY(20);
    	amplitudeSlider3.setValue(waveForms[2].getAmplitude());
    	if (this.level.getWaveCount() < 3)
    		amplitudeSlider3.setDisabled(true);
    	stage.addActor(amplitudeSlider3);
    	
    	Slider frequencySlider4 = new Slider(SoundMapping.MIN_FREQUENCY, SoundMapping.MAX_FREQUENCY, 1, true, game.getSkin(), "yellow");
    	frequencySlider4.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[3].setFrequency((int)(((Slider)actor).getValue()));
        			updateSound();
        		}
        	});

    	frequencySlider4.setWidth(45);
    	frequencySlider4.setHeight(180);
    	frequencySlider4.setX(470);
    	frequencySlider4.setY(20);
    	frequencySlider4.setValue(waveForms[3].getFrequency());
    	if (this.level.getWaveCount() < 4)
    		frequencySlider4.setDisabled(true);
    	stage.addActor(frequencySlider4);
    	
    	Slider amplitudeSlider4 = new Slider(SoundMapping.MIN_AMPLITUDE, SoundMapping.MAX_AMPLITUDE, 1, true, game.getSkin(), "yellow");
    	amplitudeSlider4.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
        			waveForms[3].setAmplitude((int)(((Slider)actor).getValue()));
        			updateSound();
        		}
        	});

    	amplitudeSlider4.setWidth(45);
    	amplitudeSlider4.setHeight(180);
    	amplitudeSlider4.setX(525);
    	amplitudeSlider4.setY(20);
    	amplitudeSlider4.setValue(waveForms[3].getAmplitude());
    	if (this.level.getWaveCount() < 4)
    		amplitudeSlider4.setDisabled(true);
    	stage.addActor(amplitudeSlider4);

    	Slider volumeSlider = new Slider(0f, 0.5f, 0.05f, true, game.getSkin(), "black");
    	volumeSlider.addListener(new ChangeListener() {
        		@Override
        		public void changed(ChangeEvent event, Actor actor)
        		{
                	if (game.getSoundManager().isEnabled()) {
                		audioDevice.setVolume(((Slider)actor).getValue());

                	}
        		}
        	});

    	volumeSlider.setWidth(45);
    	volumeSlider.setHeight(180);
    	volumeSlider.setX(700);
    	volumeSlider.setY(20);
    	volumeSlider.setValue(0);
    	stage.addActor(volumeSlider);
    	
    	this.timeLabel = new Label("0:00", game.getSkin(), "lcd");
    	this.timeLabel.setX(610);
    	this.timeLabel.setY(435);
    	this.timeLabel.setWidth(100);
    	this.timeLabel.setAlignment(Align.right);
    	stage.addActor(this.timeLabel);
    	
    	
    	
    }
    
    @Override
    public void render(float delta)
    {
        super.render(delta);
        boolean exact = false;
        
        //render time
        long sec = stopWatch.getElapsedTimeSecs();
        long min = sec / 60;
        sec = sec - (min * 60);
        this.timeLabel.setText(String.valueOf(min) + ":" + ((sec < 10) ? "0" : "") + String.valueOf(sec) );
        
        //render waves;
        waveRenderer.begin(ShapeType.Line);
        
        for(int i = 0; i < this.level.getWaveCount(); i++) {
        	int centerLine = waveForms[i].getCenterLine();
        	int width = waveForms[i].getWidth();
        	float frequency = (float)SoundMapping.Frequencies[waveForms[i].getFrequency()];
        	int left = waveForms[i].getLeft();
        	double amplitude = waveForms[i].getAmplitude();
        	waveRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
        	waveRenderer.line((float)(left), centerLine, (float)(left + width), centerLine);
        	
        	if (i == 0)
        		waveRenderer.setColor(0, 0, 1, 1);
        	else if (i == 1)
        		waveRenderer.setColor(1, 0, 0, 1);
        	else if (i == 2)
        		waveRenderer.setColor(0, 1, 0, 1);
        	else if (i == 3)
        		waveRenderer.setColor(1, 1, 0, 1);

        	for(int j = 0; j < waveForms[i].getWidth() - 1; j++) {
        		float a = (float)(Math.sin((j * this.WAVESCALE * frequency) / (float)width) * amplitude);
        		float b = (float)(Math.sin(((j + 1) * this.WAVESCALE * frequency) / (float)width) * amplitude);
        		waveRenderer.line((float)(j + left), centerLine + a, (float)(j + left + 1), centerLine + b);
        	}
        }
        int maxAmp = this.OUTPUT_HEIGHT / 2;
        int minAmp = maxAmp * -1;
        int centerLine = this.OUTPUT_TOP - maxAmp;

        waveRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
    	waveRenderer.line((float)(this.OUTPUT_LEFT), centerLine, (float)(this.OUTPUT_LEFT + this.OUTPUT_WIDTH), centerLine);

    	if (targetWaveForms != null){
    		exact = true;
    		
	    	for(int j = 0; j < this.OUTPUT_WIDTH - 1; j++) {
	    		float a = (float)
	    				(Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[0].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[0].getAmplitude()
	    	    		+ Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[1].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[1].getAmplitude()
	    	    	    + Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[2].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[2].getAmplitude()
	    	    	    + Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[3].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[3].getAmplitude()
	    						);
	    		float b = (float)
	    				(Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[0].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[0].getAmplitude()
	    	    	    + Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[1].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[1].getAmplitude()
	    	    	    + Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[2].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[2].getAmplitude()
	    	    	    + Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[targetWaveForms[3].getFrequency()]) / (float)this.OUTPUT_WIDTH) * targetWaveForms[3].getAmplitude()
	    	    				);
	
	    		float c = (float)
	    				(Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[waveForms[0].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[0].getAmplitude()
	    	    		+ Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[waveForms[1].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[1].getAmplitude()
	    	    	    + Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[waveForms[2].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[2].getAmplitude()
	    	    	    + Math.sin((j * this.WAVESCALE * SoundMapping.Frequencies[waveForms[3].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[3].getAmplitude()
	    						);
	    		float d = (float)
	    				(Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[waveForms[0].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[0].getAmplitude()
	    	    	    + Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[waveForms[1].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[1].getAmplitude()
	    	    	    + Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[waveForms[2].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[2].getAmplitude()
	    	    	    + Math.sin(((j + 1) * this.WAVESCALE * SoundMapping.Frequencies[waveForms[3].getFrequency()]) / (float)this.OUTPUT_WIDTH) * waveForms[3].getAmplitude()
	    	    				);
	
	    		if (a > maxAmp)
	    			a = maxAmp;
	    		if (a < minAmp)
	    			a = minAmp;
	    		if (b > maxAmp)
	    			b = maxAmp;
	    		if (b < minAmp)
	    			b = minAmp;
	    		if (c > maxAmp)
	    			c = maxAmp;
	    		if (c < minAmp)
	    			c = minAmp;
	    		if (d > maxAmp)
	    			d = maxAmp;
	    		if (d < minAmp)
	    			d = minAmp;
	    		
	            waveRenderer.setColor(0f, 0.3f, 0.7f, 1f);
	    		waveRenderer.line((float)(j + this.OUTPUT_LEFT), centerLine + a, (float)(j + this.OUTPUT_LEFT + 1), centerLine + b);
	            waveRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
	    		waveRenderer.line((float)(j + this.OUTPUT_LEFT), centerLine + c, (float)(j + this.OUTPUT_LEFT + 1), centerLine + d);
	    		
	    		if (Math.round(a) != Math.round(c))
	    			exact = false;
	    	}

    	}
    	

        waveRenderer.end();
        
    	if (exact == true) {
    		levelComplete = true;
    		stopWatch.stop();
        	audioRunning = false;
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
        	int medal = 3;
        	if ((int)stopWatch.getElapsedTimeSecs() <= level.getGoldTime())
        		medal = 1;
        	else if ((int)stopWatch.getElapsedTimeSecs() <= level.getSilverTime())
        		medal = 2;
    		game.setScreen(new LevelCompleteScreen(game, level.getId(), level.getName(), stopWatch.getElapsedTimeSecs(), medal));
    	}
        
    }
 
    public void dispose()
    {
    	
    	stopWatch.stop();
    	audioDevice.dispose();
    	super.dispose();
    }
    
}
