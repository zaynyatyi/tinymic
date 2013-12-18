package tinymic;

import org.haxe.lime.GameActivity;
import org.haxe.lime.HaxeObject;
import org.haxe.extension.Extension;
import android.util.Log;
import android.media.MediaRecorder;
import java.io.IOException;

class TinyMic extends Extension implements MicrophoneInputListener
{
	static private TinyMic instance;
	static private boolean isRegistered;

    static final private double EMA_FILTER = 0.6;

    private static MediaRecorder mRecorder = null;
    private static double mEMA = 0.0;

	private static MicrophoneInput micInput;

	private static double mOffsetdB = 10;  // Offset for bar, i.e. 0 lit LEDs at 10 dB.
	// The Google ASR input requirements state that audio input sensitivity
	// should be set such that 90 dB SPL at 1000 Hz yields RMS of 2500 for
	// 16-bit samples, i.e. 20 * log_10(2500 / mGain) = 90.
	private static double mGain = 2500.0 / Math.pow(10.0, 90.0 / 20.0);
	// For displaying error in calibration.
	private static double mDifferenceFromNominal = 0.0;
	private static double mRmsSmoothed = 3;  // Temporally filtered version of RMS.
	private static double mAlpha = 0.9;  // Coefficient of IIR smoothing filter for RMS.
	private static int mSampleRate = 8000;  // The audio sampling rate to use.
	private static int mAudioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;  // The audio source to use.
	
	static private HaxeObject eventHaxeHandler = null;
	static private double srmsdB = 0.0;

	public static void start() 
	{
		registerExtension();
        Log.e("tinymic", "registered extension in start()");
        micInput.setSampleRate(mSampleRate);
		micInput.setAudioSource(mAudioSource);
		micInput.start();
    }
    
    public static void stop() 
    {
        micInput.stop();
    }
    
    public static double getAmplitude() 
    {
        return srmsdB;
    }
    
    public static void assignCallbackObject(HaxeObject eventHaxeHandler)
	{
		TinyMic.eventHaxeHandler = eventHaxeHandler;
	}
    
	public static void registerExtension()
    {
        if (isRegistered) return;
        GameActivity.getInstance().registerExtension(TinyMic.getInstance());
        isRegistered = true;
        micInput = new MicrophoneInput(TinyMic.getInstance());
    }
	
	private static TinyMic getInstance()
    {
		if (instance == null)
		    instance = new TinyMic();
		return instance;
    }
    
	/**
	*  This method gets called by the micInput object owned by this activity.
	*  It first computes the RMS value and then it sets up a bit of
	*  code/closure that runs on the UI thread that does the actual drawing.
	*/
	@Override
	public void processAudioFrame(short[] audioFrame) {
		Log.e("tinymic", "buffer arrived");
		// Compute the RMS value. (Note that this does not remove DC).
		double rms = 0;
		for (int i = 0; i < audioFrame.length; i++) {
			rms += audioFrame[i]*audioFrame[i];
		}
		rms = Math.sqrt(rms/audioFrame.length);

		// Compute a smoothed version for less flickering of the display.
		mRmsSmoothed = mRmsSmoothed * mAlpha + (1 - mAlpha) * rms;
		final double rmsdB = 20.0 * Math.log10(mGain * mRmsSmoothed);
		Log.e("tinymic", "rmsdB = " + rmsdB);
		if(TinyMic.eventHaxeHandler != null) {
			srmsdB = rmsdB;
			TinyMic.eventHaxeHandler.callD0("traceDbm");
		}
	}
}
