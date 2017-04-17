#include "JKStdafx.h"

long long JKDate::GetCalendarTime(){
	time_t lt;  
	lt =time(NULL);  
	return lt;
}

long long JKDate::GetExactCalendarTime(){
	struct timeval tv;    
	gettimeofday(&tv,NULL);    
	return (long long)tv.tv_sec * (long long)1000 + (long long)tv.tv_usec / (long long)1000;
}

int JKDate::GetOneTime(long long lTime , int nType)
{
	time_t t;  
	struct tm *p;  
	t=lTime/1000;  
	p = localtime(&t);
	char tmp[64];   
	switch (nType)
	{
	case 0:
		strftime( tmp, sizeof(tmp), "%Y",p ); 
		break;
	case 1:
		strftime( tmp, sizeof(tmp), "%m",p ); 
		break;
	case 2:
		strftime( tmp, sizeof(tmp), "%d",p ); 
		break;
	case 3:
		strftime( tmp, sizeof(tmp), "%w",p );
		if (tmp[0] == '0')
			tmp[0] = '7';
		break;
	case 4:
		strftime( tmp, sizeof(tmp), "%H",p ); 
		break;
	case 5:
		strftime( tmp, sizeof(tmp), "%M",p ); 
		break;
	case 6:
		strftime( tmp, sizeof(tmp), "%S",p ); 
		break;
	}
	return JKConvert::toInt(tmp);
}

long long JKDate::GetTime(string tTime)
{
	if (tTime.size() == 8)
	{
		struct tm p; 
		long long lTime = JKConvert::toLong(tTime);
		p.tm_year = lTime/10000 - 1900;
		lTime %= 10000;
		p.tm_mon = (lTime / 100) - 1;
		lTime %= 100;
		p.tm_mday = lTime;
		p.tm_hour = 0;
		p.tm_min = 0;
		p.tm_sec = 0;
		p.tm_isdst = 0;
		time_t t_of_day;
		t_of_day=mktime(&p);
		return t_of_day*1000;
	}
	else if (tTime.size() == 14)
	{
		struct tm p; 
		long long lTime = JKConvert::toLong(tTime);
		p.tm_year = lTime/10000000000 - 1900;
		lTime %= 10000000000;
		p.tm_mon = (lTime / 100000000) - 1;
		lTime %= 100000000;
		p.tm_mday = lTime/1000000;
		lTime %= 1000000;
		p.tm_hour = lTime/10000;
		lTime %= 10000;
		p.tm_min = lTime/100;
		lTime %= 100;
		p.tm_sec = lTime;
		p.tm_isdst = 0;
		time_t t_of_day;
		t_of_day=mktime(&p);
		long long lTimeDay = t_of_day;
		return lTimeDay*1000;
	}
	return 0;
}

string JKDate::GetTime(long long lTime)
{
	lTime /= 1000;
	time_t t;  
	struct tm *p;  
	t=lTime;  
	p = localtime(&t);
	char tmp[64]; 
	strftime( tmp, sizeof(tmp), "%Y%m%d%H%M%S",p );
	return tmp;
}


string JKDate::GetLongDate(bool bUse)
{
	return GetLongDate(bUse,JKDate::GetExactCalendarTime());
}

string JKDate::GetLongDate(bool bUse , long long lTime)
{
	lTime /= 1000;
	time_t t;  
	struct tm *p;  
	t=lTime;  
	p = localtime(&t);
	string tData = "";
	tData.append(JKConvert::toString(p->tm_year+1900)).append(bUse ? "年" : "-")
		.append(JKConvert::toString(p->tm_mon+1)).append(bUse ? "月" : "-")
		.append(JKConvert::toString(p->tm_mday)).append(bUse ? "日" : "");
	return tData;
}

string JKDate::GetLongTime()
{	
	return GetLongTime(JKDate::GetExactCalendarTime());
}

string JKDate::GetLongTime(long long lTime)
{
	lTime /= 1000;
	time_t t;  
	struct tm *p;  
	t=lTime;  
	p = localtime(&t);
	char tmp[64]; 
	string tData = "";
	tData.append("%H").append(":")
		.append("%M").append(":")
		.append("%S");
	strftime( tmp, sizeof(tmp), tData.c_str() ,p );
	return tmp;
}

string JKDate::GetFullDate(bool bUse)
{
	return GetFullDate(bUse,JKDate::GetExactCalendarTime());
}

string JKDate::GetFullDate(bool bUse , long long lTime)
{
	return GetLongDate(lTime,bUse).append(" ").append(GetLongTime(lTime));
}

bool JKDate::IsLegalDate(string tTime)
{
	if (tTime.size() == 8)
		tTime.append("000000");
	long long lTime = GetTime(tTime);
	string tBack = GetTime(lTime);
	if (tTime == tBack)
		return true;
	else
		return false;
}
