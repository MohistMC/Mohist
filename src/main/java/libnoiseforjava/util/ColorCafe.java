/*
 * Copyright (C) 2003, 2004 Jason Bevins (original libnoise code)
 * Copyright © 2010 Thomas J. Hodge (java port of libnoise)
 * 
 * This file is part of libnoiseforjava.
 * 
 * libnoiseforjava is a Java port of the C++ library libnoise, which may be found at 
 * http://libnoise.sourceforge.net/.  libnoise was developed by Jason Bevins, who may be 
 * contacted at jlbezigvins@gmzigail.com (for great email, take off every 'zig').
 * Porting to Java was done by Thomas Hodge, who may be contacted at
 * libnoisezagforjava@gzagmail.com (remove every 'zag').
 * 
 * libnoiseforjava is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * libnoiseforjava is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * libnoiseforjava.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package libnoiseforjava.util;

public class ColorCafe
{
   /// Defines a color.
   ///
   /// A color object contains four 8-bit channels: red, green, blue, and an
   /// alpha (transparency) channel.  Channel values range from 0 to 255.
   ///
   /// The alpha channel defines the transparency of the color.  If the alpha
   /// channel has a value of 0, the color is completely transparent.  If the
   /// alpha channel has a value of 255, the color is completely opaque.
 
   /// Value of the alpha (transparency) channel.
   int alpha;

   /// Value of the blue channel.
   int blue;

   /// Value of the green channel.
   int green;

   /// Value of the red channel.
   int red;
   
   /// Constructor.
   ///
   /// @param r Value of the red channel.
   /// @param g Value of the green channel.
   /// @param b Value of the blue channel.
   /// @param a Value of the alpha (transparency) channel.
   public ColorCafe (int red, int green, int blue, int alpha)
   {
      this.red = red;
      this.blue = blue;
      this.green = green; 
      this.alpha = alpha;
   }

   public int getAlpha()
   {
      return alpha;
   }

   public int getBlue()
   {
      return blue;
   }

   public int getGreen()
   {
      return green;
   }

   public int getRed()
   {
      return red;
   }

   public void setAlpha(int alpha)
   {
      this.alpha = alpha;
   }

   public void setBlue(int blue)
   {
      this.blue = blue;
   }

   public void setGreen(int green)
   {
      this.green = green;
   }

   public void setRed(int red)
   {
      this.red = red;
   }

}
