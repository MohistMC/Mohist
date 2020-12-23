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

public class RendererNormalMap
{
   /// Renders a normal map from a noise map.
   ///
   /// This class renders an image containing the normal vectors from a noise
   /// map object.  This image can then be used as a bump map for a 3D
   /// application or game.
   ///
   /// This class encodes the (x, y, z) components of the normal vector into
   /// the (red, green, blue) channels of the image.  Like any 24-bit
   /// true-color image, the channel values range from 0 to 255.  0
   /// represents a normal coordinate of -1.0 and 255 represents a normal
   /// coordinate of +1.0.
   ///
   /// You should also specify the <i>bump height</i> before rendering the
   /// normal map.  The bump height specifies the ratio of spatial resolution
   /// to elevation resolution.  For example, if your noise map has a spatial
   /// resolution of 30 meters and an elevation resolution of one meter, set
   /// the bump height to 1.0 / 30.0.
   ///
   /// <b>Rendering the normal map</b>
   ///
   /// To render the image containing the normal map, perform the following
   /// steps:
   /// - Pass a NoiseMap object to the setSourceNoiseMap() method.
   /// - Pass an ImageCafe object to the setDestImage() method.
   /// - Call the render() method.

   /// The bump height for the normal map.
   double bumpHeight;

   /// A flag specifying whether wrapping is enabled.
   boolean isWrapEnabled;

   /// A pointer to the destination image.
   ImageCafe destImageCafe;

   /// A pointer to the source noise map.
   NoiseMap sourceNoiseMap;


   public RendererNormalMap () throws ExceptionInvalidParam
   {
      bumpHeight = 1.0;
      isWrapEnabled = false;
      destImageCafe = new ImageCafe(0,0);
      sourceNoiseMap = new NoiseMap(0,0);
   }

   public RendererNormalMap (int height, int width) throws ExceptionInvalidParam
   {
      bumpHeight = 1.0;
      isWrapEnabled = false;
      destImageCafe = new ImageCafe(height, width);
      sourceNoiseMap = new NoiseMap(height, width);
   }

   /// Calculates the normal vector at a given point on the noise map.
   ///
   /// @param nc The height of the given point in the noise map.
   /// @param nr The height of the left neighbor.
   /// @param nu The height of the up neighbor.
   /// @param bumpHeight The bump height.
   ///
   /// @returns The normal vector represented as a color.
   ///
   /// This method encodes the (x, y, z) components of the normal vector
   /// into the (red, green, blue) channels of the returned color.  In
   /// order to represent the vector as a color, each coordinate of the
   /// normal is mapped from the -1.0 to 1.0 range to the 0 to 255 range.
   ///
   /// The bump height specifies the ratio of spatial resolution to
   /// elevation resolution.  For example, if your noise map has a
   /// spatial resolution of 30 meters and an elevation resolution of one
   /// meter, set the bump height to 1.0 / 30.0.
   /// 
   /// The spatial resolution and elevation resolution are determined by
   /// the application.
   public ColorCafe calcNormalColor (double nc, double nr, double nu,
         double bumpHeight)
   {
      // Calculate the surface normal.
      nc *= bumpHeight;
      nr *= bumpHeight;
      nu *= bumpHeight;
      double ncr = (nc - nr);
      double ncu = (nc - nu);
      double d = Math.sqrt ((ncu * ncu) + (ncr * ncr) + 1);
      double vxc = (nc - nr) / d;
      double vyc = (nc - nu) / d;
      double vzc = 1.0 / d;

      // Map the normal range from the (-1.0 .. +1.0) range to the (0 .. 255)
      // range.
      int xc, yc, zc;
      xc = (int) (Math.floor((vxc + 1.0) * 127.5)) & 0xff;
      yc = (int) (Math.floor((vyc + 1.0) * 127.5)) & 0xff;
      zc = (int) (Math.floor((vzc + 1.0) * 127.5)) & 0xff;

      // left as example of what was here in case above conversion doesn't work.
      //zc = (noise::uint8)((noise::uint)((floor)((vzc + 1.0) * 127.5)) & 0xff);

      return new ColorCafe (xc, yc, zc, 255);
   }

   /// Renders the noise map to the destination image.
   ///
   /// @pre setSourceNoiseMap() has been previously called.
   /// @pre setDestImage() has been previously called.
   ///
   /// @post The original contents of the destination image is destroyed.
   ///
   /// @throw ExceptionInvalidParam See the preconditions.
   public void render () throws ExceptionInvalidParam
   {
      if ( sourceNoiseMap == null
            || destImageCafe == null
            || sourceNoiseMap.getWidth  () <= 0
            || sourceNoiseMap.getHeight () <= 0)
         throw new ExceptionInvalidParam ("Invalid Parameter in RendererNormalMap");


      int width  = sourceNoiseMap.getWidth  ();
      int height = sourceNoiseMap.getHeight ();

      for (int y = 0; y < height; y++)
      {
         for (int x = 0; x < width; x++)
         {
            // Calculate the positions of the current point's right and up
            // neighbors.
            int xRightOffset, yUpOffset;
            if (isWrapEnabled)
            {
               if (x == (int)width - 1)
                  xRightOffset = -((int)width - 1);
               else
                  xRightOffset = 1;

               if (y == (int)height - 1)
                  yUpOffset = -((int)height - 1);
               else
                  yUpOffset = 1;
            }
            else
            {
               if (x == (int)width - 1)
                  xRightOffset = 0;
               else
                  xRightOffset = 1;

               if (y == (int)height - 1)
                  yUpOffset = 0;
               else
                  yUpOffset = 1;

            }

            // Get the noise value of the current point in the source noise map
            // and the noise values of its right and up neighbors.
            double nc = (double)(sourceNoiseMap.getValue(x, y));
            double nr = (double)(sourceNoiseMap.getValue((x + xRightOffset),y));
            double nu = (double)(sourceNoiseMap.getValue(x, (y + yUpOffset)));

            // Calculate the normal product.
            destImageCafe.setValue(x,y, (calcNormalColor (nc, nr, nu, bumpHeight)));

            // Go to the next point.
            //++pSource;
            //++pDest;
         }
      }
   }

   /// Enables or disables noise-map wrapping.
   ///
   /// @param enable A flag that enables or disables noise-map wrapping.
   ///
   /// This object requires three points (the initial point and the right
   /// and up neighbors) to calculate the normal vector at that point.
   /// If wrapping is/ enabled, and the initial point is on the edge of
   /// the noise map, the appropriate neighbors that lie outside of the
   /// noise map will "wrap" to the opposite side(s) of the noise map.
   /// Otherwise, the appropriate neighbors are cropped to the edge of
   /// the noise map.
   ///
   /// Enabling wrapping is useful when creating spherical and tileable
   /// normal maps.
   public void enableWrap (boolean enable)
   {
      isWrapEnabled = enable;
   }

   /// Returns the bump height.
   ///
   /// @returns The bump height.
   ///
   /// The bump height specifies the ratio of spatial resolution to
   /// elevation resolution.  For example, if your noise map has a
   /// spatial resolution of 30 meters and an elevation resolution of one
   /// meter, set the bump height to 1.0 / 30.0.
   ///
   /// The spatial resolution and elevation resolution are determined by
   /// the application.
   public double getBumpHeight ()
   {
      return bumpHeight;
   }

   /// Determines if noise-map wrapping is enabled.
   ///
   /// @returns
   /// - @a true if noise-map wrapping is enabled.
   /// - @a false if noise-map wrapping is disabled.
   ///
   /// This object requires three points (the initial point and the right
   /// and up neighbors) to calculate the normal vector at that point.
   /// If wrapping is/ enabled, and the initial point is on the edge of
   /// the noise map, the appropriate neighbors that lie outside of the
   /// noise map will "wrap" to the opposite side(s) of the noise map.
   /// Otherwise, the appropriate neighbors are cropped to the edge of
   /// the noise map.
   ///
   /// Enabling wrapping is useful when creating spherical and tileable
   /// normal maps.
   public boolean isWrapEnabled ()
   {
      return isWrapEnabled;
   }

   /// Sets the bump height.
   ///
   /// @param bumpHeight The bump height.
   ///
   /// The bump height specifies the ratio of spatial resolution to
   /// elevation resolution.  For example, if your noise map has a
   /// spatial resolution of 30 meters and an elevation resolution of one
   /// meter, set the bump height to 1.0 / 30.0.
   ///
   /// The spatial resolution and elevation resolution are determined by
   /// the application.
   public void setBumpHeight (double bumpHeight)
   {
      this.bumpHeight = bumpHeight;
   }

   /// Sets the destination image.
   ///
   /// @param destImage The destination image.
   ///
   /// The destination image will contain the normal map after a
   /// successful call to the render() method.
   ///
   /// The destination image must exist throughout the lifetime of this
   /// object unless another image replaces that image.
   public void setDestImage (ImageCafe destImage)
   {
      this.destImageCafe = destImage;
   }

   /// Sets the source noise map.
   ///
   /// @param sourceNoiseMap The source noise map.
   ///
   /// The destination image must exist throughout the lifetime of this
   /// object unless another image replaces that image.
   public void setSourceNoiseMap (NoiseMap sourceNoiseMap)
   {
      this.sourceNoiseMap = sourceNoiseMap;
   }

   public ImageCafe getDestImageCafe()
   {
      return destImageCafe;
   }

   public NoiseMap getSourceNoiseMap()
   {
      return sourceNoiseMap;
   }

   public void setDestImageCafe(ImageCafe destImageCafe)
   {
      this.destImageCafe = destImageCafe;
   }

}
