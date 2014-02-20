package dk.aau.cs.giraf.cars.gamecode;

import javax.microedition.khronos.opengles.GL10;

public interface Drawer {

    // SpriteBatcher V1.1
    // Author: Tim Wicksteed
    // www.ionage.co.uk
    // An object implementing this interface must be handed to SpriteBatcher
    // when it is created. The onDrawFrame method of SpriteBatcher automatically
    // calls the method onDrawFrame() of the object implementing Drawer.

    // All draw calls onto GameRenderer should be made from within this method.

	/* License

	Copyright (c) 2013 Tim Wicksteed
	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the “Software”), 
	to deal in the Software without restriction, including without limitation the 
	rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
	sell copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all 
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHOR OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
	DEALINGS IN THE SOFTWARE. */


    public void onDrawFrame(GL10 gl, GameRenderer spriteBatcher);

}
