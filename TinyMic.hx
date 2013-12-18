package;

#if cpp
import cpp.Lib;
#elseif neko
import neko.Lib;
#end


class TinyMic {
	private static var inited:Bool = false;
	
	public static function init():Void
	{
		init_media();
	}
	
	public static function startMeasure()
	{
		tinymic_start();
	}
	
	public static function stopMeasure()
	{
		tinymic_stop();
	}
	
	public static function assignCallbackObject(haxeObject:Dynamic):Void
	{
		tinymic_assign_callback_object(haxeObject);
	}
	
	public static function getAmplitude():Float
	{
		return tinymic_get_amplitude();
	}
	
	private static function init_media():Void {
		if(inited)
			return;
		tinymic_start = openfl.utils.JNI.createStaticMethod(
	        "tinymic.TinyMic",
	        "start",
	        "()V"
	    );
	    tinymic_stop = openfl.utils.JNI.createStaticMethod(
	        "tinymic.TinyMic",
	        "stop",
	        "()V"
	    );
	    tinymic_assign_callback_object = openfl.utils.JNI.createStaticMethod(
	    	"tinymic.TinyMic",
	    	"assignCallbackObject",
	    	"(Lorg/haxe/lime/HaxeObject;)V"
	    );
	    tinymic_get_amplitude = openfl.utils.JNI.createStaticMethod(
	    	"tinymic.TinyMic",
	    	"getAmplitude",
	    	"()D"
	    );
	    inited = true;
	}

	private static var tinymic_start : Dynamic;
	private static var tinymic_stop : Dynamic;
	private static var tinymic_assign_callback_object : Dynamic;
	private static var tinymic_get_amplitude : Dynamic;
}
