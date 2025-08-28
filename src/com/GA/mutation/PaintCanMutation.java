package com.GA.mutation;

import com.GA.IndividualImage;
import com.utils.ImageUtils;
import com.utils.Util;

public class PaintCanMutation extends MutationFunction {

    public IndividualImage mutate(IndividualImage image) {
        int r = Util.rng.nextInt(256);
        int g = Util.rng.nextInt(256);
        int b = Util.rng.nextInt(256);
        int y = Util.rng.nextInt(image.getImage().getHeight());
        int x = Util.rng.nextInt(image.getImage().getWidth());
        IndividualImage out = new IndividualImage(ImageUtils.paintCan(image.getImage(), x, y, r, g, b));
        return out;
    }
}
