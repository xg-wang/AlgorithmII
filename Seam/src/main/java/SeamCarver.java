import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Xingan Wang on 11/5/17.
 */
public class SeamCarver {

    private Element[][] pic;
    private int w;
    private int h;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture is null!");
        }
        w = picture.width();
        h = picture.height();
        pic = new Element[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                pic[i][j] = new Element(picture.get(i, j));
            }
        }
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                calculate(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture;
        picture = new Picture(w, h);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                picture.set(i, j, pic[i][j].getColor());
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return w;
    }

    // height of current picture
    public int height() {
        return h;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= w || y < 0 || y >= h) {
            throw new IllegalArgumentException("row and col illegal!");
        }
        if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
            return 1000;
        }
        return pic[x][y].getEnergy();
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (h <= 2) {
            int[] ret = new int[w];
            Arrays.fill(ret, 0);
            return ret;
        }
        HashMap<Integer, Double> idxSum = new HashMap<>();
        HashMap<Integer, Stack<Integer>> idxPath = new HashMap<>();
        for (int i = 1; i < h - 1; i++) {
            idxPath.put(i, new Stack<>());
            idxSum.put(i, 0.0);
        }
        double defaultSum = 1000000 * w;
        for (int i = w - 1; i > 0; i--) {
            HashMap<Integer, Stack<Integer>> newPaths = new HashMap<>();
            HashMap<Integer, Double> newSums = new HashMap<>();
            for (int j = 1; j < h - 1; j++) {
                int minIdx = j;
                double minE2 = idxSum.get(j);
                if (idxSum.getOrDefault(j - 1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(j - 1, defaultSum);
                    minIdx = j - 1;
                }
                if (idxSum.getOrDefault(j + 1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(j + 1, defaultSum);
                    minIdx = j + 1;
                }
                newSums.put(j, pic[i][j].getEnergy() + minE2);
                Stack<Integer> newPath = (Stack<Integer>) idxPath.get(minIdx).clone();
                newPath.push(j);
                newPaths.put(j, newPath);
            }
            idxPath = newPaths;
            idxSum = newSums;
        }
        int minIdx = 1;
        for (int i = 2; i < h; i++) {
            if (idxSum.getOrDefault(i, defaultSum) < idxSum.get(minIdx)) {
                minIdx = i;
            }
        }
        Stack<Integer> path = idxPath.get(minIdx);
        path.push(minIdx);
        int[] pathArray = new int[path.size()];
        int i = 0;
        while (!path.empty()) {
            pathArray[i++] = path.pop();
        }
        return pathArray;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (w <= 2) {
            int[] ret = new int[h];
            Arrays.fill(ret, 0);
            return ret;
        }
        HashMap<Integer, Double> idxSum = new HashMap<>();
        HashMap<Integer, Stack<Integer>> idxPath = new HashMap<>();
        for (int i = 1; i < w - 1; i++) {
            idxPath.put(i, new Stack<>());
            idxSum.put(i, 0.0);
        }
        double defaultSum = 1000000 * h;
        for (int j = h - 1; j > 0; j--) {
            HashMap<Integer, Stack<Integer>> newPaths = new HashMap<>();
            HashMap<Integer, Double> newSums = new HashMap<>();
            for (int i = 1; i < w - 1; i++) {
                int minIdx = i;
                double minE2 = idxSum.get(i);
                if (idxSum.getOrDefault(i - 1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(i - 1, defaultSum);
                    minIdx = i - 1;
                }
                if (idxSum.getOrDefault(i + 1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(i + 1, defaultSum);
                    minIdx = i + 1;
                }
                newSums.put(i, pic[i][j].getEnergy() + minE2);
                Stack<Integer> newPath = (Stack<Integer>) idxPath.get(minIdx).clone();
                newPath.push(i);
                newPaths.put(i, newPath);
            }
            idxPath = newPaths;
            idxSum = newSums;
        }
        int minIdx = 1;
        for (int i = 2; i < w; i++) {
            if (idxSum.getOrDefault(i, defaultSum) < idxSum.get(minIdx)) {
                minIdx = i;
            }
        }
        Stack<Integer> path = idxPath.get(minIdx);
        path.push(minIdx);
        int[] pathArray = new int[path.size()];
        int i = 0;
        while (!path.empty()) {
            pathArray[i++] = path.pop();
        }
        return pathArray;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("seam is null");
        if (seam.length != w) throw new IllegalArgumentException("seam wrong length");
        if (h <= 1) throw new IllegalArgumentException("image height " + h + " invalid");
        int prev = seam[0];
        for (int i = 0; i < w; i++) {
            int s = seam[i];
            if (s < 0 || s >= h || Math.abs(s - prev) > 1) {
                throw new IllegalArgumentException("invalid seam");
            }
            prev = s;
            if (s < h - 1) {
                System.arraycopy(pic[i], s + 1, pic[i], s, h - s - 1);
            }
        }
        h--;
        for (int i = 1; i < w - 1; i++) {
            int s = seam[i];
            if (s > 1) calculate(i, s - 1);
            if (s < h - 2) calculate(i, s + 1);
            if (s > 0 && s < h - 1) calculate(i, s);
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("seam is null");
        if (seam.length != h) throw new IllegalArgumentException("seam wrong length");
        if (w <= 1) throw new IllegalArgumentException("image width " + w + " invalid");
        int prev = seam[0];
        for (int j = 0; j < h; j++) {
            int s = seam[j];
            if (s < 0 || s >= w || Math.abs(s - prev) > 1) {
                throw new IllegalArgumentException("invalid seam");
            }
            prev = s;
            for (int i = s + 1; i < w; i++) {
                pic[i - 1][j] = pic[i][j];
            }
        }
        w--;
        for (int j = 1; j < h - 1; j++) {
            int s = seam[j];
            if (s > 1) calculate(s - 1, j);
            if (s < w - 2) calculate(s + 1, j);
            if (s > 0 && s < w - 1) calculate(s, j);
        }
    }

    private void calculate(int i, int j) {
        pic[i][j].calculateEnergy2(
                pic[i + 1][j].getColor(),
                pic[i - 1][j].getColor(),
                pic[i][j + 1].getColor(),
                pic[i][j - 1].getColor());
    }
}
