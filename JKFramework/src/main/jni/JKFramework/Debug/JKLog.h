#ifndef JKLOG_H_
#define JKLOG_H_

#include <string>

using namespace std;


class JKLog{
private:		//变量
	static string tDebugtTag;
public:		//函数
	static void d(string tMsg);
	static void e(string tMsg);
};


#endif /* JKLOG_H_ */
