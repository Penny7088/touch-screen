#include "JKStdafx.h"

static struct sigaction old_sa[NSIG];

void JKException::ExitProgram()
{
	exit(0);
}

void JKException::android_sigaction(int signal, siginfo_t *info, void *reserved)
{
//    JNIEnv* env = JKDebug::GetJNIEvent();
//
//	vector<string> a_tType;
//	a_tType.push_back("void");
//	jvalue v[0];
//
//	JKJni::DoJavaFunction(NULL,"com/jkframework/config/JKSystem","NativeCrash",a_tType,v);
//
//	JKDebug::ReleaseJNIEvent();
//    JKLog::e("我崩溃了");
    old_sa[signal].sa_handler(signal);
}

void JKException::InitCrash()
{
    struct sigaction handler;
    memset(&handler, 0, sizeof(struct sigaction));

    handler.sa_sigaction = android_sigaction;
    handler.sa_flags = SA_RESETHAND;

    #define CATCHSIG(X) sigaction(X, &handler, &old_sa[X])
        CATCHSIG(SIGILL);
        CATCHSIG(SIGABRT);
        CATCHSIG(SIGBUS);
        CATCHSIG(SIGFPE);
        CATCHSIG(SIGSEGV);
//        CATCHSIG(SIGSTKFLT);
        CATCHSIG(SIGPIPE);
}