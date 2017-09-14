package com.cjw.util;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;

public class GLScreen {

	/**
	 * 取得屏幕像素
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap getScreenBitmap(int x, int y, int w, int h) {
		int b[] = new int[w * h];
		int bt[] = new int[w * h];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		GLES20.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int pix = b[i * w + j];
				int pb = (pix >> 16) & 0xff;
				int pr = (pix << 16) & 0x00ff0000;
				int pix1 = (pix & 0xff00ff00) | pr | pb;
				bt[(h - i - 1) * w + j] = pix1;
			}
		}
		Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);

		return sb;
	}
	
}
