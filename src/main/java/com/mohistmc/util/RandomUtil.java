package com.mohistmc.util;

public class RandomUtil {



    public void generateNoise(int[] noiseData, int rows, int cols) {
    }

    public void GenerateWhiteNoise(int width, int height) //White Gen
    {
    }

    public float[][] GenerateSmoothNoise(float[][] baseNoise, int octave)
    {

        return baseNoise;
    }

    public float Interpolate(float x0, float x1, float alpha)
    {
        return x0 * (1 - alpha) + alpha * x1;
    }

    public float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount)
    {
        int width = baseNoise.length;
        int height = baseNoise[0].length;

        float[][][] smoothNoise = new float[octaveCount][][]; //an array of 2D arrays containing

        float persistance = 0.5f; // default value is 0.5f

        //generate smooth noise
        for (int i = 0; i < octaveCount; i++)
        {
            smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
        }

        float[][] perlinNoise = new float[width][height];
        float amplitude = 1.0f;
        float totalAmplitude = 0.0f;

        //blend noise together
        for (int octave = octaveCount - 1; octave >= 0; octave--)
        {
            amplitude *= persistance;
            totalAmplitude += amplitude;

            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
                }
            }
        }

        //normalization
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                perlinNoise[i][j] /= totalAmplitude;
            }
        }

        return perlinNoise;
    }

    void MapGray(float[][] perlinNoise)
    {
        int width = perlinNoise.length;
        int height = perlinNoise[0].length;
        float[][] format = new float[width][height];
        int ta=0, tr=0, tb=0, tg=0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                ta = 255;
                int u = (int)(perlinNoise[i][j] * (float)80.0);
                tr = u+100;
                tg = u+100;
                tb = u+100;
                ta = (int)(255.0f * perlinNoise[i][j]);

            }
        }

    }

}