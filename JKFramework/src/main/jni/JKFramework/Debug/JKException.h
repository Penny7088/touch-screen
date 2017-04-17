#ifndef JKEXCEPTION_H_
#define JKEXCEPTION_H_

#include <signal.h>

class JKException{
public:
	/**退出应用程序*/
	static void ExitProgram();
	/**初始化异常捕捉*/
	static void InitCrash();
private:
    /**android信号函数*/
    static void android_sigaction(int signal, siginfo_t *info, void *reserved);
};

#endif /* JKEXCEPTION_H_ */
