/*
 * Copyright (C) 2007-2014 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.jkframework.bean;

import android.os.Build;

public enum JKDeviceType {
	GENERIC,
	KINDLE_FIRE_1ST_GENERATION,
	KINDLE_FIRE_2ND_GENERATION,
	KINDLE_FIRE_HD,
	EKEN_M001,
	PAN_DIGITAL;

	private static JKDeviceType ourInstance;
	public static JKDeviceType Instance() {
		if (ourInstance == null) {
			if ("Amazon".equals(Build.MANUFACTURER)) {
				if ("Kindle Fire".equals(Build.MODEL)) {
					ourInstance = KINDLE_FIRE_1ST_GENERATION;
				} else if ("KFOT".equals(Build.MODEL)) {
					ourInstance = KINDLE_FIRE_2ND_GENERATION;
				} else if (Build.DISPLAY != null && Build.DISPLAY.contains("simenxie")) {
					ourInstance = EKEN_M001;
				} else if ("PD_Novel".equals(Build.MODEL)) {
					ourInstance = PAN_DIGITAL;
				} else {
					ourInstance = KINDLE_FIRE_HD;
				}			
			} else {
				ourInstance = GENERIC;
			}
		}
		return ourInstance;
	}
	
}
