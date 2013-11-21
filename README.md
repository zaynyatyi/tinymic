tinymic
=======

Just small openfl java extension with callback

usage
=====

To compile ndll use:

<pre>
cd project
haxelib run hxcpp Build.xml
haxelib run hxcpp Build.xml -Dandroid
</pre>

Add the extension to haxelib:

<pre>
haxelib dev TinyMicPATH_TO_THE_EXTENSION_ROOT
</pre>

Usage in project:

<pre>
Add 
    <haxelib name="TinyMic" /> to project.xml
Add 
    <uses-permission android:name="android.permission.RECORD_AUDIO" /> to your manifest file
In class you should have claaback function 
    public function traceDbm():Void
After main class creation use
    TinyMic.init(); // to init JNI functions
    TinyMic.assignCallbackObject(this); // to assign callback with traceDbm():Void function
    TinyMic.startMeasure(); // to start measuring dB
    TinyMic.stopMeasure(); // to stop measuring dB
</pre>

