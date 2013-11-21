#ifndef STATIC_LINK
#define IMPLEMENT_API
#endif

#if defined(HX_WINDOWS) || defined(HX_MACOS) || defined(HX_LINUX)
#define NEKO_COMPATIBLE
#endif


#include <hx/CFFI.h>
#include "Utils.h"


using namespace tinymic;



extern "C" void tinymic_main () {
	
	val_int(0); // Fix Neko init
	
}
DEFINE_ENTRY_POINT (tinymic_main);



extern "C" int tinymic_register_prims () { return 0; }