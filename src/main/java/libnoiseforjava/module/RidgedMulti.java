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

package libnoiseforjava.module;

import libnoiseforjava.NoiseGen;
import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.exception.ExceptionInvalidParam;

public class RidgedMulti extends ModuleBase
{
   /// Noise module that outputs 3-dimensional ridged-multifractal noise.
   ///
   /// This noise module, heavily based on the Perlin-noise module, generates
   /// ridged-multifractal noise.  Ridged-multifractal noise is generated in
   /// much of the same way as Perlin noise, except the output of each octave
   /// is modified by an absolute-value function.  Modifying the octave
   /// values in this way produces ridge-like formations.
   ///
   /// Ridged-multifractal noise does not use a persistence value.  This is
   /// because the persistence values of the octaves are based on the values
   /// generated from from previous octaves, creating a feedback loop (or
   /// that's what it looks like after reading the code.)
   ///
   /// This noise module outputs ridged-multifractal-noise values that
   /// usually range from -1.0 to +1.0, but there are no guarantees that all
   /// output values will exist within that range.
   ///
   /// @note For ridged-multifractal noise generated with only one octave,
   /// the output value ranges from -1.0 to 0.0.
   ///
   /// Ridged-multifractal noise is often used to generate craggy mountainous
   /// terrain or marble-like textures.
   ///
   /// This noise module does not require any source modules.
   ///
   /// <b>Octaves</b>
   ///
   /// The number of octaves control the <i>amount of detail</i> of the
   /// ridged-multifractal noise.  Adding more octaves increases the detail
   /// of the ridged-multifractal noise, but with the drawback of increasing
   /// the calculation time.
   ///
   /// An application may specify the number of octaves that generate
   /// ridged-multifractal noise by calling the setOctaveCount() method.
   ///
   /// <b>Frequency</b>
   ///
   /// An application may specify the frequency of the first octave by
   /// calling the setFrequency() method.
   ///
   /// <b>Lacunarity</b>
   ///
   /// The lacunarity specifies the frequency multipler between successive
   /// octaves.
   ///
   /// The effect of modifying the lacunarity is subtle; you may need to play
   /// with the lacunarity value to determine the effects.  For best results,
   /// set the lacunarity to a number between 1.5 and 3.5.
   ///
   /// <b>References &amp; Acknowledgments</b>
   ///
   /// <a href=http://www.texturingandmodeling.com/Musgrave.html>F.
   /// Kenton "Doc Mojo" Musgrave's texturing page</a> - This page contains
   /// links to source code that generates ridged-multfractal noise, among
   /// other types of noise.  The source file <a
   /// href=http://www.texturingandmodeling.com/CODE/MUSGRAVE/CLOUD/fractal.c>
   /// fractal.c</a> contains the code I used in my ridged-multifractal class
   /// (see the @a RidgedMultifractal() function.)  This code was written by F.
   /// Kenton Musgrave, the person who created
   /// <a href=http://www.pandromeda.com/>MojoWorld</a>.  He is also one of
   /// the authors in <i>Texturing and Modeling: A Procedural Approach</i>
   /// (Morgan Kaufmann, 2002. ISBN 1-55860-848-6.)

   /// Default frequency for the noise::module::RidgedMulti noise module.
   static final double DEFAULT_RIDGED_FREQUENCY = 1.0;

   /// Default lacunarity for the noise::module::RidgedMulti noise module.
   static final double DEFAULT_RIDGED_LACUNARITY = 2.0;

   /// Default number of octaves for the noise::module::RidgedMulti noise
   /// module.
   static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;

   /// Default noise quality for the noise::module::RidgedMulti noise
   /// module.
   static final NoiseQuality DEFAULT_RIDGED_QUALITY = NoiseQuality.QUALITY_STD;

   /// Default noise seed for the noise::module::RidgedMulti noise module.
   static final int DEFAULT_RIDGED_SEED = 0;

   /// Maximum number of octaves for the noise::module::RidgedMulti noise
   /// module.
   static final int RIDGED_MAX_OCTAVE = 30;

   /// Frequency of the first octave.
   double frequency;

   /// Frequency multiplier between successive octaves.
   double lacunarity;

   /// Quality of the ridged-multifractal noise.
   NoiseQuality noiseQuality;

   /// Total number of octaves that generate the ridged-multifractal
   /// noise.
   int octaveCount;

   /// Contains the spectral weights for each octave.
   double [] spectralWeights = new double[RIDGED_MAX_OCTAVE];

   /// Seed value used by the ridged-multfractal-noise function.
   int seed;


   public RidgedMulti ()
   {
      super(0);
      frequency = DEFAULT_RIDGED_FREQUENCY;
      lacunarity = DEFAULT_RIDGED_LACUNARITY;
      noiseQuality = DEFAULT_RIDGED_QUALITY;
      octaveCount = DEFAULT_RIDGED_OCTAVE_COUNT;
      seed = DEFAULT_RIDGED_SEED;

      calcSpectralWeights();
   }

   // Calculates the spectral weights for each octave.
   public void calcSpectralWeights ()
   {
      // This exponent parameter should be user-defined; it may be exposed in a
      // future version of libnoise.
      double h = 1.0;

      double frequency = 1.0;
      for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
         // Compute weight for each frequency.
         this.spectralWeights[i] = Math.pow (frequency, -h);
         frequency *= lacunarity;
      }
   }

   // Multifractal code originally written by F. Kenton "Doc Mojo" Musgrave,
   // 1998.  Modified by jas for use with libnoise.
   public double getValue (double x, double y, double z)
   {
      x *= frequency;
      y *= frequency;
      z *= frequency;

      double signal = 0.0;
      double value  = 0.0;
      double weight = 1.0;

      // These parameters should be user-defined; they may be exposed in a
      // future version of libnoiseforjava.
      double offset = 1.0;
      double gain = 2.0;

      for (int curOctave = 0; curOctave < octaveCount; curOctave++)
      {
         // Make sure that these floating-point values have the same range as a 32-
         // bit integer so that we can pass them to the coherent-noise functions.
         double nx, ny, nz;
         nx = NoiseGen.MakeInt32Range (x);
         ny = NoiseGen.MakeInt32Range (y);
         nz = NoiseGen.MakeInt32Range (z);

         // Get the coherent-noise value.
         int curSeed = (seed + curOctave) & 0x7fffffff;
         signal = NoiseGen.GradientCoherentNoise3D (nx, ny, nz, curSeed, noiseQuality);

         // Make the ridges.
         signal = Math.abs (signal);
         signal = offset - signal;

         // Square the signal to increase the sharpness of the ridges.
         signal *= signal;

         // The weighting from the previous octave is applied to the signal.
         // Larger values have higher weights, producing sharp points along the
         // ridges.
         signal *= weight;

         // Weight successive contributions by the previous signal.
         weight = signal * gain;
         if (weight > 1.0)
            weight = 1.0;
         if (weight < 0.0)
            weight = 0.0;


         // Add the signal to the output value.
         value += (signal * spectralWeights[curOctave]);

         // Go to the next octave.
         x *= lacunarity;
         y *= lacunarity;
         z *= lacunarity;
      }

      return (value * 1.25) - 1.0;
   }

   public double getFrequency ()
   {
      return frequency;
   }

   /// Returns the lacunarity of the ridged-multifractal noise.
   ///
   /// @returns The lacunarity of the ridged-multifractal noise.
   /// 
   /// The lacunarity is the frequency multiplier between successive
   /// octaves.
   public double getLacunarity ()
   {
      return lacunarity;
   }

   /// Returns the quality of the ridged-multifractal noise.
   ///
   /// @returns The quality of the ridged-multifractal noise.
   ///
   /// See noise::NoiseQuality for definitions of the various
   /// coherent-noise qualities.
   public NoiseQuality getNoiseQuality ()
   {
      return noiseQuality;
   }

   /// Returns the number of octaves that generate the
   /// ridged-multifractal noise.
   ///
   /// @returns The number of octaves that generate the
   /// ridged-multifractal noise.
   ///
   /// The number of octaves controls the amount of detail in the
   /// ridged-multifractal noise.
   public int getOctaveCount ()
   {
      return octaveCount;
   }

   /// Returns the seed value used by the ridged-multifractal-noise
   /// function.
   ///
   /// @returns The seed value.
   public int getSeed ()
   {
      return seed;
   }


   /// Sets the frequency of the first octave.
   ///
   /// @param frequency The frequency of the first octave.
   public void setFrequency (double frequency)
   {
      this.frequency = frequency;
   }

   /// Sets the lacunarity of the ridged-multifractal noise.
   ///
   /// @param lacunarity The lacunarity of the ridged-multifractal noise.
   /// 
   /// The lacunarity is the frequency multiplier between successive
   /// octaves.
   ///
   /// For best results, set the lacunarity to a number between 1.5 and
   /// 3.5.
   public void setLacunarity (double lacunarity)
   {
      this.lacunarity = lacunarity;
      calcSpectralWeights ();
   }

   /// Sets the quality of the ridged-multifractal noise.
   ///
   /// @param noiseQuality The quality of the ridged-multifractal noise.
   ///
   /// See NoiseQuality for definitions of the various
   /// coherent-noise qualities.
   public void setNoiseQuality (NoiseQuality noiseQuality)
   {
      this.noiseQuality = noiseQuality;
   }

   /// Sets the number of octaves that generate the ridged-multifractal
   /// noise.
   ///
   /// @param octaveCount The number of octaves that generate the
   /// ridged-multifractal noise.
   ///
   /// @pre The number of octaves ranges from 1 to RIDGED_MAX_OCTAVE.
   ///
   /// @throw ExceptionInvalidParam An invalid parameter was
   /// specified; see the preconditions for more information.
   ///
   /// The number of octaves controls the amount of detail in the
   /// ridged-multifractal noise.
   ///
   /// The larger the number of octaves, the more time required to
   /// calculate the ridged-multifractal-noise value.
   public void setOctaveCount (int octaveCount) throws ExceptionInvalidParam
   {
      if (octaveCount > RIDGED_MAX_OCTAVE)
      {
         throw new ExceptionInvalidParam ("An invalid parameter was passed" +
         " to a libnoise function or method.");
      }

      this.octaveCount = octaveCount;
   }

   /// Sets the seed value used by the ridged-multifractal-noise
   /// function.
   ///
   /// @param seed The seed value.
   public void setSeed (int seed)
   {
      this.seed = seed;
   }

   public double[] getSpectralWeights()
   {
      return spectralWeights;
   }

   public void setSpectralWeights(double[] spectralWeights)
   {
      this.spectralWeights = spectralWeights;
   }

}
