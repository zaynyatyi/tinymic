package tinymic;

import org.haxe.lime.GameActivity;
import org.haxe.lime.HaxeObject;
import org.haxe.extension.Extension;
import android.util.Log;
import android.media.MediaRecorder;
import java.io.IOException;
import android.os.Environment;

class TinyMic extends Extension
{
	static private TinyMic instance;
	static private boolean isRegistered;

    static final private double EMA_FILTER = 0.6;

    private static MediaRecorder mRecorder = null;
    private static double mEMA = 0.0;
	
	static private HaxeObject eventHaxeHandler = null;

	public static void start() 
	{
		registerExtension();
        Log.e("tinymic", "registered extension in start()");
		if (mRecorder == null) {
	        mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        mRecorder.setOutputFile("/dev/null/");
	       
			try {
		        mRecorder.prepare();
			} catch (IllegalStateException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
			} catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
			}
			mRecorder.start();
			mEMA = 0.0;
        }
    }
    
    public static void stop() 
    {
        if (mRecorder != null) {
	        mRecorder.stop();       
	        mRecorder.release();
	        mRecorder = null;
        }
    }
    
    public static double getAmplitude() 
    {
    	if(mRecorder != null) {
    		double amp = (mRecorder.getMaxAmplitude()/270.0);
            mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
            return mEMA;
        } else {
            return 1;
        }
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
    }
	
	private static TinyMic getInstance()
    {
		if (instance == null)
		    instance = new TinyMic();
		return instance;
    }
}
