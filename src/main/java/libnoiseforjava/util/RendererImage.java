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

import libnoiseforjava.Interp;
import libnoiseforjava.exception.ExceptionInvalidParam;

public class RendererImage
{

   /// Renders an image from a noise map.
   ///
   /// This class renders an image given the contents of a noise-map object.
   ///
   /// An application can configure the output of the image in three ways:
   /// - Specify the color gradient.
   /// - Specify the light source parameters.
   /// - Specify the background image.
   ///
   /// <b>Specify the color gradient</b>
   ///
   /// This class uses a color gradient to calculate the color for each pixel
   /// in the destination image according to the value from the corresponding
   /// position in the noise map.
   ///
   /// A color gradient is a list of gradually-changing colors.  A color
   /// gradient is defined by a list of <i>gradient points</i>.  Each
   /// gradient point has a position and a color.  In a color gradient, the
   /// colors between two adjacent gradient points are linearly interpolated.
   ///
   /// For example, suppose this class contains the following color gradient:
   ///
   /// - -1.0 maps to dark blue.
   /// - -0.2 maps to light blue.
   /// - -0.1 maps to tan.
   /// - 0.0 maps to green.
   /// - 1.0 maps to white.
   ///
   /// The value 0.5 maps to a greenish-white color because 0.5 is halfway
   /// between 0.0 (mapped to green) and 1.0 (mapped to white).
   ///
   /// The value -0.6 maps to a medium blue color because -0.6 is halfway
   /// between -1.0 (mapped to dark blue) and -0.2 (mapped to light blue).
   ///
   /// The color gradient requires a minimum of two gradient points.
   ///
   /// This class contains two pre-made gradients: a grayscale gradient and a
   /// color gradient suitable for terrain.  To use these pre-made gradients,
   /// call the buildGrayscaleGradient() or buildTerrainGradient() methods,
   /// respectively.
   ///
   /// @note The color value passed to addGradientPoint() has an alpha
   /// channel.  This alpha channel specifies how a pixel in the background
   /// image (if specified) is blended with the calculated color.  If the
   /// alpha value is high, this class weighs the blend towards the
   /// calculated color, and if the alpha value is low, this class weighs the
   /// blend towards the color from the corresponding pixel in the background
   /// image.
   ///
   /// <b>Specify the light source parameters</b>
   ///
   /// This class contains a parallel light source that lights the image.  It
   /// interprets the noise map as a bump map.
   ///
   /// To enable or disable lighting, pass a Boolean value to the
   /// enableLight() method.
   ///
   /// To set the position of the light source in the "sky", call the
   /// setLightAzimuth() and setLightElev() methods.
   ///
   /// To set the color of the light source, call the setLightColor() method.
   ///
   /// To set the intensity of the light source, call the setLightIntensity()
   /// method.  A good intensity value is 2.0, although that value tends to
   /// "wash out" very light colors from the image.
   /// 
   /// To set the contrast amount between areas in light and areas in shadow,
   /// call the setLightContrast() method.  Determining the correct contrast
   /// amount requires some trial and error, but if your application
   /// interprets the noise map as a height map that has its elevation values
   /// measured in meters and has a horizontal resolution of @a h meters, a
   /// good contrast amount to use is ( 1.0 / @a h ).
   /// 
   /// <b>Specify the background image</b>
   ///
   /// To specify a background image, pass an Image object to the
   /// setBackgroundImage() method.
   ///
   /// This class determines the color of a pixel in the destination image by
   /// blending the calculated color with the color of the corresponding
   /// pixel from the background image.
   ///
   /// The blend amount is determined by the alpha of the calculated color.
   /// If the alpha value is high, this class weighs the blend towards the
   /// calculated color, and if the alpha value is low, this class weighs the
   /// blend towards the color from the corresponding pixel in the background
   /// image.
   ///
   /// <b>Rendering the image</b>
   ///
   /// To render the image, perform the following steps:
   /// - Pass a NoiseMap object to the setSourceNoiseMap() method.
   /// - Pass an ImageCafe object to the setDestImage() method.
   /// - Pass an ImageCafe object to the setBackgroundImage() method (optional)
   /// - Call the render() method.   


   static final double SQRT_2 = 1.4142135623730950488;


   /// The cosine of the azimuth of the light source.
   double cosAzimuth;

   /// The cosine of the elevation of the light source.
   double cosElev;

   /// The color gradient used to specify the image colors.
   GradientColor gradient;

   /// A flag specifying whether lighting is enabled.
   boolean isLightEnabled;

   /// A flag specifying whether wrapping is enabled.
   boolean isWrapEnabled;

   /// The azimuth of the light source, in degrees.
   double lightAzimuth;

   /// The brightness of the light source.
   double lightBrightness;

   /// The color of the light source.
   ColorCafe lightColor;

   /// The contrast between areas in light and areas in shadow.
   double lightContrast;

   /// The elevation of the light source, in degrees.
   double lightElev;

   /// The intensity of the light source.
   double lightIntensity;

   /// A pointer to the background image.
   ImageCafe backgroundImage;

   /// A pointer to the destination image.
   ImageCafe destImageCafe;

   /// A pointer to the source noise map.
   NoiseMap sourceNoiseMap;

   /// Used by the calcLightIntensity() method to recalculate the light
   /// values only if the light parameters change.
   ///
   /// When the light parameters change, this value is set to True.  When
   /// the calcLightIntensity() method is called, this value is set to
   /// false.
   boolean recalcLightValues;

   /// The sine of the azimuth of the light source.
   double sinAzimuth;

   /// The sine of the elevation of the light source.
   double sinElev;


   public RendererImage () throws ExceptionInvalidParam
   {
      isLightEnabled = false;
      isWrapEnabled = false;
      lightAzimuth = 45.0;
      lightBrightness = 1.0;
      lightColor = new ColorCafe(255, 255, 255, 255);
      lightContrast = 1.0;
      lightElev = 45.0;
      lightIntensity = 1.0;
      backgroundImage = null;
      destImageCafe = null;
      sourceNoiseMap = null;
      recalcLightValues = true;

      buildGrayscaleGradient ();
   }

   /// Adds a gradient point to this gradient object.
   ///
   /// @param gradientPos The position of this gradient point.
   /// @param gradientColor The color of this gradient point.
   ///
   /// @pre No two gradient points have the same position.
   ///
   /// @throw noise::ExceptionInvalidParam See the preconditions.
   ///
   /// This object uses a color gradient to calculate the color for each
   /// pixel in the destination image according to the value from the
   /// corresponding position in the noise map.
   ///
   /// The gradient requires a minimum of two gradient points.
   ///
   /// The specified color value passed to this method has an alpha
   /// channel.  This alpha channel specifies how a pixel in the
   /// background image (if specified) is blended with the calculated
   /// color.  If the alpha value is high, this object weighs the blend
   /// towards the calculated color, and if the alpha value is low, this
   /// object weighs the blend towards the color from the corresponding
   /// pixel in the background image.
   public void addGradientPoint (double gradientPos, ColorCafe gradientColor) throws ExceptionInvalidParam
   {
      gradient.addGradientPoint (gradientPos, gradientColor);
   }

   /// Builds a grayscale gradient.
   ///
   /// @post The original gradient is cleared and a grayscale gradient is
   /// created.
   ///
   /// This color gradient contains the following gradient points:
   /// - -1.0 maps to black
   /// - 1.0 maps to white
   public void buildGrayscaleGradient () throws ExceptionInvalidParam
   {
      clearGradient ();
      gradient.addGradientPoint (-1.0, new ColorCafe (  0,   0,   0, 255));
      gradient.addGradientPoint ( 1.0, new ColorCafe (255, 255, 255, 255));
   }

   /// Builds a color gradient suitable for terrain.
   ///
   /// @post The original gradient is cleared and a terrain gradient is
   /// created.
   ///
   /// This gradient color at position 0.0 is the "sea level".  Above
   /// that value, the gradient contains greens, browns, and whites.
   /// Below that value, the gradient contains various shades of blue.
   public void buildTerrainGradient () throws ExceptionInvalidParam
   {
      clearGradient ();
      gradient.addGradientPoint (-1.00, new ColorCafe (  0,   0, 128, 255));
      gradient.addGradientPoint (-0.20, new ColorCafe ( 32,  64, 128, 255));
      gradient.addGradientPoint (-0.04, new ColorCafe ( 64,  96, 192, 255));
      gradient.addGradientPoint (-0.02, new ColorCafe (192, 192, 128, 255));
      gradient.addGradientPoint ( 0.00, new ColorCafe (  0, 192,   0, 255));
      gradient.addGradientPoint ( 0.25, new ColorCafe (192, 192,   0, 255));
      gradient.addGradientPoint ( 0.50, new ColorCafe (160,  96,  64, 255));
      gradient.addGradientPoint ( 0.75, new ColorCafe (128, 255, 255, 255));
      gradient.addGradientPoint ( 1.00, new ColorCafe (255, 255, 255, 255));
   }

   /// Calculates the destination color.
   ///
   /// @param sourceColor The source color generated from the color
   /// gradient.
   /// @param backgroundColor The color from the background image at the
   /// corresponding position.
   /// @param lightValue The intensity of the light at that position.
   ///
   /// @returns The destination color.
   public ColorCafe calcDestColor (ColorCafe sourceColor, ColorCafe backgroundColor,
         double lightValue)
   {
      double sourceRed   = (double)sourceColor.red   / 255.0;
      double sourceGreen = (double)sourceColor.green / 255.0;
      double sourceBlue  = (double)sourceColor.blue  / 255.0;
      double sourceAlpha = (double)sourceColor.alpha / 255.0;
      double backgroundRed   = (double)backgroundColor.red   / 255.0;
      double backgroundGreen = (double)backgroundColor.green / 255.0;
      double backgroundBlue  = (double)backgroundColor.blue  / 255.0;

      // First, blend the source color to the background color using the alpha
      // of the source color.
      double red   = Interp.linearInterp (backgroundRed,   sourceRed  , sourceAlpha);
      double green = Interp.linearInterp (backgroundGreen, sourceGreen, sourceAlpha);
      double blue  = Interp.linearInterp (backgroundBlue,  sourceBlue , sourceAlpha);

      if (isLightEnabled)
      {
         // Now calculate the light color.
         double lightRed   = lightValue * (double)lightColor.red   / 255.0;
         double lightGreen = lightValue * (double)lightColor.green / 255.0;
         double lightBlue  = lightValue * (double)lightColor.blue  / 255.0;

         // Apply the light color to the new color.
         red   *= lightRed  ;
         green *= lightGreen;
         blue  *= lightBlue ;
      }

      // Clamp the color channels to the (0..1) range.
      red   = (red   < 0.0)? 0.0: red  ;
      red   = (red   > 1.0)? 1.0: red  ;
      green = (green < 0.0)? 0.0: green;
      green = (green > 1.0)? 1.0: green;
      blue  = (blue  < 0.0)? 0.0: blue ;
      blue  = (blue  > 1.0)? 1.0: blue ;

      // Rescale the color channels to the noise::uint8 (0..255) range and return
      // the new color.
      ColorCafe newColor = new ColorCafe((int)(red * 255.0) & 0xff,(int)(green * 255.0) & 0xff,
            (int)(blue  * 255.0) & 0xff, Math.max (sourceColor.alpha, backgroundColor.alpha));
      return newColor;
   }

   /// Calculates the intensity of the light given some elevation values.
   ///
   /// @param center Elevation of the center point.
   /// @param left Elevation of the point directly left of the center
   /// point.
   /// @param right Elevation of the point directly right of the center
   /// point.
   /// @param down Elevation of the point directly below the center
   /// point.
   /// @param up Elevation of the point directly above the center point.
   ///
   /// These values come directly from the noise map.
   public double calcLightIntensity (double center, double left,
         double right, double down, double up)
   {
      // Recalculate the sine and cosine of the various light values if
      // necessary so it does not have to be calculated each time this method is
      // called.
      if (recalcLightValues) {
         cosAzimuth = Math.cos (Math.toRadians(lightAzimuth));
         sinAzimuth = Math.sin (Math.toRadians(lightAzimuth));
         cosElev    = Math.cos (Math.toRadians(lightElev));
         sinElev    = Math.sin (Math.toRadians(lightElev));
         recalcLightValues = false;
      }

      // Now do the lighting calculations.
      double I_MAX = 1.0;
      double io = I_MAX * SQRT_2 * sinElev / 2.0;
      double ix = (I_MAX - io) * lightContrast * SQRT_2 * cosElev
      * cosAzimuth;
      double iy = (I_MAX - io) * lightContrast * SQRT_2 * cosElev
      * sinAzimuth; 
      double intensity = (ix * (left - right) + iy * (down - up) + io);

      if (intensity < 0.0)
         intensity = 0.0;

      return intensity;
   }

   /// Clears the color gradient.
   ///
   /// Before calling the render() method, the application must specify a
   /// new color gradient with at least two gradient points.
   public void clearGradient ()
   {
      gradient = new GradientColor();
      gradient.clear();
   }

   /// Renders the destination image using the contents of the source
   /// noise map and an optional background image.
   ///
   /// @pre setSourceNoiseMap() has been previously called.
   /// @pre setDestImage() has been previously called.
   /// @pre There are at least two gradient points in the color gradient.
   /// @pre No two gradient points have the same position.
   /// @pre If a background image was specified, it has the exact same
   /// size as the source height map.
   ///
   /// @post The original contents of the destination image is destroyed.
   ///
   /// @throw ExceptionInvalidParam See the preconditions.
   ///
   /// The background image and the destination image can safely refer to
   /// the same image, although in this case, the destination image is
   /// irretrievably blended into the background image.
   public void render () throws ExceptionInvalidParam
   {
      if ( sourceNoiseMap == null
            || destImageCafe == null
            || sourceNoiseMap.getWidth  () <= 0
            || sourceNoiseMap.getHeight () <= 0
            || gradient.getGradientPointCount () < 2)
         throw new ExceptionInvalidParam ("Invalid Parameter in RendererImage");


      int width  = sourceNoiseMap.getWidth  ();
      int height = sourceNoiseMap.getHeight ();

      // If a background image was provided, make sure it is the same size the
      // source noise map.
      if (backgroundImage != null)
         if ( backgroundImage.getWidth  () != width || backgroundImage.getHeight () != height)
            throw new ExceptionInvalidParam ("Invalid Parameter in RendererImage");


      // Create the destination image.  It is safe to reuse it if this is also the
      // background image.
      if (destImageCafe != backgroundImage)
         destImageCafe.setSize (width, height);

      for (int y = 0; y < height; y++)
      {
         ColorCafe background = new ColorCafe(255, 255, 255, 255);

         for (int x = 0; x < width; x++)
         {
            // Get the color based on the value at the current point in the noise
            // map.
            ColorCafe destColor = gradient.getColor (sourceNoiseMap.getValue(x,y));

            // If lighting is enabled, calculate the light intensity based on the
            // rate of change at the current point in the noise map.
            double lightIntensity;
            if (isLightEnabled)
            {
               // Calculate the positions of the current point's four-neighbors.
               int xLeftOffset, xRightOffset;
               int yUpOffset  , yDownOffset ;
               if (isWrapEnabled)
               {
                  if (x == 0)
                  {
                     xLeftOffset  = (int)width - 1;
                     xRightOffset = 1;}
                  else if (x == (int)width - 1)
                  {
                     xLeftOffset  = -1;
                     xRightOffset = -((int)width - 1);
                  }
                  else
                  {
                     xLeftOffset  = -1;
                     xRightOffset = 1;
                  }

                  if (y == 0)
                  {
                     yDownOffset = (int)height - 1;
                     yUpOffset   = 1;
                  } 
                  else if (y == (int)height - 1) 
                  {
                     yDownOffset = -1;
                     yUpOffset   = -((int)height - 1);
                  } 
                  else
                  {
                     yDownOffset = -1;
                     yUpOffset   = 1;
                  }
               } 
               else
               {
                  if (x == 0)
                  {
                     xLeftOffset  = 0;
                     xRightOffset = 1;
                  } 
                  else if (x == (int)width - 1)
                  {
                     xLeftOffset  = -1;
                     xRightOffset = 0;
                  } 
                  else
                  {
                     xLeftOffset  = -1;
                     xRightOffset = 1;
                  }

                  if (y == 0) 
                  {
                     yDownOffset = 0;
                     yUpOffset   = 1;
                  }
                  else if (y == (int)height - 1)
                  {
                     yDownOffset = -1;
                     yUpOffset   = 0;
                  }
                  else 
                  {
                     yDownOffset = -1;
                     yUpOffset   = 1;
                  }
               }

               // Get the noise value of the current point in the source noise map
               // and the noise values of its four-neighbors.
               double nc = (double)(sourceNoiseMap.getValue(x,y));
               double nl = (double)(sourceNoiseMap.getValue(x + xLeftOffset, y));
               double nr = (double)(sourceNoiseMap.getValue(x + xRightOffset, y));
               double nd = (double)(sourceNoiseMap.getValue(x,y + yDownOffset));
               double nu = (double)(sourceNoiseMap.getValue(x,y + yUpOffset));

               // Now we can calculate the lighting intensity.
               lightIntensity = calcLightIntensity (nc, nl, nr, nd, nu);
               lightIntensity *= lightBrightness;

            }
            else
            {
               // These values will apply no lighting to the destination image.
               lightIntensity = 1.0;
            }

            // Get the current background color from the background image.
            ColorCafe backgroundColor = new ColorCafe (255, 255, 255, 255);
            if (backgroundImage != null)
               backgroundColor = backgroundImage.getValue(x, y);

            // Blend the destination color, background color, and the light
            // intensity together, then update the destination image with that
            // color.
            destImageCafe.setValue(x, y, calcDestColor (destColor, backgroundColor, lightIntensity));
         }
      }
   }

   /// Enables or disables the light source.
   ///
   /// @param enable A flag that enables or disables the light source.
   ///
   /// If the light source is enabled, this object will interpret the
   /// noise map as a bump map.
   public void enableLight (boolean enable)
   {
      isLightEnabled = enable;
   }

   /// Enables or disables noise-map wrapping.
   ///
   /// @param enable A flag that enables or disables noise-map wrapping.
   ///
   /// This object requires five points (the initial point and its four
   /// neighbors) to calculate light shading.  If wrapping is enabled,
   /// and the initial point is on the edge of the noise map, the
   /// appropriate neighbors that lie outside of the noise map will
   /// "wrap" to the opposite side(s) of the noise map.  Otherwise, the
   /// appropriate neighbors are cropped to the edge of the noise map.
   ///
   /// Enabling wrapping is useful when creating spherical renderings and
   /// tileable textures.
   public void enableWrap (boolean enable)
   {
      isWrapEnabled = enable;
   }

   /// Returns the azimuth of the light source, in degrees.
   ///
   /// @returns The azimuth of the light source.
   ///
   /// The azimuth is the location of the light source around the
   /// horizon:
   /// - 0.0 degrees is east.
   /// - 90.0 degrees is north.
   /// - 180.0 degrees is west.
   /// - 270.0 degrees is south.
   public double getLightAzimuth ()
   {
      return lightAzimuth;
   }

   /// Returns the brightness of the light source.
   ///
   /// @returns The brightness of the light source.
   public double getLightBrightness ()
   {
      return lightBrightness;
   }

   /// Returns the color of the light source.
   ///
   /// @returns The color of the light source.
   public ColorCafe getLightColor ()
   {
      return lightColor;
   }

   /// Returns the contrast of the light source.
   ///
   /// @returns The contrast of the light source.
   ///
   /// The contrast specifies how sharp the boundary is between the
   /// light-facing areas and the shadowed areas.
   ///
   /// The contrast determines the difference between areas in light and
   /// areas in shadow.  Determining the correct contrast amount requires
   /// some trial and error, but if your application interprets the noise
   /// map as a height map that has a spatial resolution of @a h meters
   /// and an elevation resolution of 1 meter, a good contrast amount to
   /// use is ( 1.0 / @a h ).
   public double getLightContrast ()
   {
      return lightContrast;
   }

   /// Returns the elevation of the light source, in degrees.
   ///
   /// @returns The elevation of the light source.
   ///
   /// The elevation is the angle above the horizon:
   /// - 0 degrees is on the horizon.
   /// - 90 degrees is straight up.
   public double getLightElev ()
   {
      return lightElev;
   }

   /// Returns the intensity of the light source.
   ///
   /// @returns The intensity of the light source.
   public double getLightIntensity ()
   {
      return lightIntensity;
   }

   /// Determines if the light source is enabled.
   ///
   /// @returns
   /// - @a true if the light source is enabled.
   /// - @a false if the light source is disabled.
   public boolean isLightEnabled ()
   {
      return isLightEnabled;
   }

   /// Determines if noise-map wrapping is enabled.
   ///
   /// @returns
   /// - @a true if noise-map wrapping is enabled.
   /// - @a false if noise-map wrapping is disabled.
   ///
   /// This object requires five points (the initial point and its four
   /// neighbors) to calculate light shading.  If wrapping is enabled,
   /// and the initial point is on the edge of the noise map, the
   /// appropriate neighbors that lie outside of the noise map will
   /// "wrap" to the opposite side(s) of the noise map.  Otherwise, the
   /// appropriate neighbors are cropped to the edge of the noise map.
   ///
   /// Enabling wrapping is useful when creating spherical renderings and
   /// tileable textures
   public boolean isWrapEnabled ()
   {
      return isWrapEnabled;
   }

   /// Sets the background image.
   ///
   /// @param backgroundImage The background image.
   ///
   /// If a background image has been specified, the Render() method
   /// blends the pixels from the background image onto the corresponding
   /// pixels in the destination image.  The blending weights are
   /// determined by the alpha channel in the pixels in the destination
   /// image.
   ///
   /// The destination image must exist throughout the lifetime of this
   /// object unless another image replaces that image.
   public void setBackgroundImage (ImageCafe backgroundImage)
   {
      this.backgroundImage = backgroundImage;
   }

   /// Sets the destination image.
   ///
   /// @param destImage The destination image.
   ///
   /// The destination image will contain the rendered image after a
   /// successful call to the Render() method.
   ///
   /// The destination image must exist throughout the lifetime of this
   /// object unless another image replaces that image.
   public void setDestImage (ImageCafe destImage)
   {
      this.destImageCafe = destImage;
   }

   /// Sets the azimuth of the light source, in degrees.
   ///
   /// @param lightAzimuth The azimuth of the light source.
   ///
   /// The azimuth is the location of the light source around the
   /// horizon:
   /// - 0.0 degrees is east.
   /// - 90.0 degrees is north.
   /// - 180.0 degrees is west.
   /// - 270.0 degrees is south.
   ///
   /// Make sure the light source is enabled via a call to the
   /// EnableLight() method before calling the Render() method.
   public void setLightAzimuth (double lightAzimuth)
   {
      this.lightAzimuth = lightAzimuth;
      this.recalcLightValues = true;
   }

   /// Sets the brightness of the light source.
   ///
   /// @param lightBrightness The brightness of the light source.
   ///
   /// Make sure the light source is enabled via a call to the
   /// EnableLight() method before calling the Render() method.
   public void setLightBrightness (double lightBrightness)
   {
      this.lightBrightness = lightBrightness;
      this.recalcLightValues = true;
   }

   /// Sets the color of the light source.
   ///
   /// @param lightColor The light color.
   ///
   /// Make sure the light source is enabled via a call to the
   /// EnableLight() method before calling the Render() method.
   public void setLightColor (ColorCafe lightColor)
   {
      this.lightColor = lightColor;
   }

   /// Sets the contrast of the light source.
   ///
   /// @param lightContrast The contrast of the light source.
   ///
   /// @pre The specified light contrast is positive.
   ///
   /// @throw noise::ExceptionInvalidParam See the preconditions.
   ///
   /// The contrast specifies how sharp the boundary is between the
   /// light-facing areas and the shadowed areas.
   ///
   /// The contrast determines the difference between areas in light and
   /// areas in shadow.  Determining the correct contrast amount requires
   /// some trial and error, but if your application interprets the noise
   /// map as a height map that has a spatial resolution of @a h meters
   /// and an elevation resolution of 1 meter, a good contrast amount to
   /// use is ( 1.0 / @a h ).
   ///
   /// Make sure the light source is enabled via a call to the
   /// EnableLight() method before calling the Render() method.
   public void setLightContrast (double lightContrast) throws ExceptionInvalidParam
   {
      if (lightContrast <= 0.0)  
         throw new ExceptionInvalidParam ("Invalid Parameter in RendererImage");


      this.lightContrast = lightContrast;
      this.recalcLightValues = true;
   }

   /// Sets the elevation of the light source, in degrees.
   ///
   /// @param lightElev The elevation of the light source.
   ///
   /// The elevation is the angle above the horizon:
   /// - 0 degrees is on the horizon.
   /// - 90 degrees is straight up.
   ///
   /// Make sure the light source is enabled via a call to the
   /// EnableLight() method before calling the Render() method.
   public void setLightElev (double lightElev)
   {
      this.lightElev = lightElev;
      this.recalcLightValues = true;
   }

   /// Sets the intensity of the light source.
   ///
   /// @returns The intensity of the light source.
   ///
   /// A good value for intensity is 2.0.
   ///
   /// Make sure the light source is enabled via a call to the
   /// enableLight() method before calling the render() method.
   public void setLightIntensity (double lightIntensity) throws ExceptionInvalidParam
   {
      if (lightIntensity < 0.0)
         throw new ExceptionInvalidParam ("Invalid Parameter in RendererImage");

      this.lightIntensity = lightIntensity;
      this.recalcLightValues = true;
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
   
}
