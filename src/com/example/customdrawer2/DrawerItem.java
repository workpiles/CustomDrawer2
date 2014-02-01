package com.example.customdrawer2;

public class DrawerItem {
	public int mIconRes;
	public String mTitle;
	public int mCounter;
	
	public DrawerItem(int icon, String title) {
		mIconRes = icon;
		mTitle = title;
		mCounter = 0;
	}

	public void countUp() {
		mCounter++;
	}
}
