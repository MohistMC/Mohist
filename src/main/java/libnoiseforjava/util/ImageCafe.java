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

import libnoiseforjava.exception.ExceptionInvalidParam;

public class ImageCafe
{

   /// Implements an image, a 2-dimensional array of color values.
   ///
   /// An image can be used to store a color texture.
   ///
   /// These color values are of type ColorCafe.
   ///
   /// The size (width and height) of the image can be specified during
   /// object construction.
   ///
   /// The getValue() and setValue() methods can be used to access individual
   /// color values stored in the image.
   ///
   ///
   /// <b>Border Values</b>
   ///
   /// All of the color values outside of the image are assumed to have a
   /// common color value known as the <i>border value</i>.
   ///
   /// To set the border value, call the setBorderValue() method.
   ///
   /// The getValue() method returns the border value if the specified
   /// position lies outside of the image.
   ///

   /// The Color value used for all positions outside of the image.
   ColorCafe borderValue;

   /// The current height of the image.
   int height;

   /// The current width of the image.
   int width;

   /// Array of ColorCafes holding the color values
   ColorCafe [][] imageCafeColors;

   public ImageCafe (int width, int height) throws ExceptionInvalidParam
   {
      setSize (width, height);
      borderValue = new ColorCafe (0, 0, 0, 0);
      imageCafeColors = new ColorCafe[width][height];
   }

   /// Returns a color value from the specified position in the image.
   ///
   /// @param x The x coordinate of the position.
   /// @param y The y coordinate of the position.
   ///
   /// @returns The color value at that position.
   ///
   /// This method returns the border value if the coordinates exist
   /// outside of the image.
   public ColorCafe getValue (int x, int y)
   {
      if (x >= 0 && x < width && y >= 0 && y < height)
         return imageCafeColors[x][y];
      else
         // The coordinates specified are outside the image.  Return the border
         // value.
         return borderValue;
   }

   /// Sets the new size for the image.
   ///
   /// @param width The new width for the image.
   /// @param height The new height for the image.
   ///
   /// @pre The width and height values are positive.
   /// @pre The width and height values do not exceed the maximum
   /// possible width and height for the image.
   ///
   /// @throw ExceptionInvalidParam See the preconditions.
   public void setSize (int width, int height) throws ExceptionInvalidParam
   {
      if (width < 0 || height < 0)
         // Invalid width or height.
         throw new ExceptionInvalidParam ("Invalid Parameter in ImageCafe");
      else
      {
         this.width  = width;
         this.height = height;
      }
   }

   /// Sets a color value at a specified position in the image.
   ///
   /// @param x The x coordinate of the position.
   /// @param y The y coordinate of the position.
   /// @param value The color value to set at the given position.
   ///
   /// This method does nothing if the image is empty or the position is
   /// outside the bounds of the image.
   public void setValue (int x, int y, ColorCafe value)
   {
      if (x >= 0 && x < width && y >= 0 && y < height)
         this.imageCafeColors[x][y] = value;
   }


   /// Returns the color value used for all positions outside of the
   /// image.
   ///
   /// @returns The color value used for all positions outside of the
   /// image.
   ///
   /// All positions outside of the image are assumed to have a common
   /// color value known as the <i>border value</i>.
   public ColorCafe getBorderValue ()
   {
      return borderValue;
   }

   /// Returns the height of the image.
   ///
   /// @returns The height of the image.
   public int getHeight ()
   {
      return height;
   }

   /// Returns the width of the image.
   ///
   /// @returns The width of the image.
   public int getWidth ()
   {
      return width;
   }

   /// Sets the color value to use for all positions outside of the
   /// image.
   ///
   /// @param borderValue The color value to use for all positions
   /// outside of the image.
   ///
   /// All positions outside of the image are assumed to have a common
   /// color value known as the <i>border value</i>.
   public void setBorderValue (ColorCafe borderValue)
   {
      this.borderValue = borderValue;
   }
}
