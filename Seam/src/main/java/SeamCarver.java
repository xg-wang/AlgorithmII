import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Xingan Wang on 11/5/17.
 */
public class SeamCarver {

    private Element[][] _pic;
    private int _w;
    private int _h;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture is null!");
        }
        _w = picture.width();
        _h = picture.height();
        _pic = new Element[_w][_h];
        for (int i = 0; i < _w; i++) {
            for (int j = 0; j < _h; j++) {
                _pic[i][j] = new Element(picture.get(i, j));
            }
        }
        for (int i = 1; i < _w - 1; i++) {
            for (int j = 1; j < _h - 1; j++) {
                _calculate(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture pic;
        pic = new Picture(_w, _h);
        for (int i = 0; i < _w; i++) {
            for (int j = 0; j < _h; j++) {
                pic.set(i, j, _pic[i][j].getColor());
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return _w;
    }

    // height of current picture
    public int height() {
        return _h;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= _w || y < 0 || y >= _h) {
            throw new IllegalArgumentException("row and col illegal!");
        }
        if (x == 0 || x == _w - 1 || y == 0 || y == _h - 1) {
            return 1000;
        }
        return Math.sqrt(_pic[x][y].getEnergy2());
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return _horizontalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return _verticalSeam();
    }

    private int[] _horizontalSeam() {
        HashMap<Integer, Integer> idxSum = new HashMap<>();
        HashMap<Integer, Stack<Integer>> idxPath = new HashMap<>();
        for (int i = 1; i < _h - 1; i++) {
            idxPath.put(i, new Stack<>());
            idxSum.put(i, 0);
        }
        int defaultSum = 1000000 * _w;
        for (int i = _w - 1; i > 0; i--) {
            HashMap<Integer, Stack<Integer>> newPaths = new HashMap<>();
            HashMap<Integer, Integer> newSums = new HashMap<>();
            for (int j = 1; j < _h - 1; j++) {
                int minIdx = j, minE2 = idxSum.get(j);
                if (idxSum.getOrDefault(j-1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(j-1, defaultSum);
                    minIdx = j - 1;
                }
                if (idxSum.getOrDefault(j+1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(j+1, defaultSum);
                    minIdx = j + 1;
                }
                newSums.put(j, _pic[i][j].getEnergy2() + minE2);
                Stack<Integer> newPath = (Stack<Integer>)idxPath.get(minIdx).clone();
                newPath.push(j);
                newPaths.put(j, newPath);
            }
            idxPath = newPaths;
            idxSum = newSums;
        }
        int minIdx = 1;
        for (int i = 2; i < _h; i++) {
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

    private int[] _verticalSeam() {
        HashMap<Integer, Integer> idxSum = new HashMap<>();
        HashMap<Integer, Stack<Integer>> idxPath = new HashMap<>();
        for (int i = 1; i < _w - 1; i++) {
            idxPath.put(i, new Stack<>());
            idxSum.put(i, 0);
        }
        int defaultSum = 1000000 * _h;
        for (int j = _h - 1; j > 0; j--) {
            HashMap<Integer, Stack<Integer>> newPaths = new HashMap<>();
            HashMap<Integer, Integer> newSums = new HashMap<>();
            for (int i = 1; i < _w - 1; i++) {
                int minIdx = i, minE2 = idxSum.get(i);
                if (idxSum.getOrDefault(i-1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(i-1, defaultSum);
                    minIdx = i - 1;
                }
                if (idxSum.getOrDefault(i+1, defaultSum) < minE2) {
                    minE2 = idxSum.getOrDefault(i+1, defaultSum);
                    minIdx = i + 1;
                }
                newSums.put(i, _pic[i][j].getEnergy2() + minE2);
                Stack<Integer> newPath = (Stack<Integer>)idxPath.get(minIdx).clone();
                newPath.push(i);
                newPaths.put(i, newPath);
            }
            idxPath = newPaths;
            idxSum = newSums;
        }
        int minIdx = 1;
        for (int i = 2; i < _w; i++) {
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
        for (int i = 1; i < _w - 1; i++) {
            int s = seam[i];
            if (s < _h - 1) {
                System.arraycopy(_pic[i], s + 1, _pic[i], s, _h - s - 1);
            }
        }
        _h--;
        for (int i = 1; i < _w - 1; i++) {
            int s = seam[i];
            if (s > 1) _calculate(i, s - 1);
            if (s < _h - 2) _calculate(i, s + 1);
            _calculate(i, s);
        }
    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        for (int j = 1; j < _h - 1; j++) {
            int s = seam[j];
            for (int i = s + 1; i < _w; i++) {
                _pic[i-1][j] = _pic[i][j];
            }
        }
        _w--;
        for (int j = 1; j < _h - 1; j++) {
            int s = seam[j];
            if (s > 1) _calculate(s - 1, j);
            if (s < _w - 2) _calculate(s + 1, j);
            _calculate(s, j);
        }
    }

    private void _calculate(int i, int j) {
        _pic[i][j].calculateEnergy2(
                _pic[i+1][j].getColor(),
                _pic[i-1][j].getColor(),
                _pic[i][j+1].getColor(),
                _pic[i][j-1].getColor());
    }
}
