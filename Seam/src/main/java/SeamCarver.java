import edu.princeton.cs.algs4.Picture;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Xingan Wang on 11/5/17.
 */
public class SeamCarver {

    private final Element[][] _pic;
    private boolean _isVertical;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture is null!");
        }
        _isVertical = true;
        _pic = new Element[picture.width()][picture.height()];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                _pic[i][j] = new Element(picture.get(i, j));
            }
        }
        for (int i = 1; i < picture.width() - 1; i++) {
            for (int j = 1; j < picture.height() - 1; j++) {
                _pic[i][j].calculateEnergy2(
                        _pic[i+1][j].getColor(),
                        _pic[i-1][j].getColor(),
                        _pic[i][j+1].getColor(),
                        _pic[i][j-1].getColor());
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture pic;
        if (_isVertical) {
            pic = new Picture(_pic.length, _pic[0].length);
            for (int i = 0; i < _pic.length; i++) {
                for (int j = 0; j < _pic[0].length; j++) {
                    pic.set(i, j, _pic[i][j].getColor());
                }
            }
        } else {
            pic = new Picture(_pic[0].length, _pic.length);
            for (int i = 0; i < _pic.length; i++) {
                for (int j = 0; j < _pic[0].length; j++) {
                    pic.set(j, i, _pic[i][j].getColor());
                }
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return _isVertical ? _pic.length : _pic[0].length;
    }

    // height of current picture
    public int height() {
        return _isVertical ? _pic[0].length : _pic.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException("row and col illegal!");
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }
        return Math.sqrt(_pic[x][y].getEnergy2());
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return _isVertical ? _horizontalSeam() : _verticalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return _isVertical ? _verticalSeam() : _horizontalSeam();
    }

    private int[] _horizontalSeam() {
        HashMap<Integer, Integer> idxSum = new HashMap<>();
        HashMap<Integer, Stack<Integer>> idxPath = new HashMap<>();
        for (int i = 1; i < _pic[0].length - 1; i++) {
            idxPath.put(i, new Stack<>());
            idxSum.put(i, 0);
        }
        int defaultSum = 1000000 * _pic.length;
        for (int i = _pic.length - 1; i > 0; i--) {
            HashMap<Integer, Stack<Integer>> newPaths = new HashMap<>();
            HashMap<Integer, Integer> newSums = new HashMap<>();
            for (int j = 1; j < _pic[0].length - 1; j++) {
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
        for (int i = 2; i < _pic[0].length; i++) {
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
        for (int i = 1; i < _pic.length - 1; i++) {
            idxPath.put(i, new Stack<>());
            idxSum.put(i, 0);
        }
        int defaultSum = 1000000 * _pic[0].length;
        for (int j = _pic[0].length - 1; j > 0; j--) {
            HashMap<Integer, Stack<Integer>> newPaths = new HashMap<>();
            HashMap<Integer, Integer> newSums = new HashMap<>();
            for (int i = 1; i < _pic.length - 1; i++) {
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
                Stack<Integer> newPath = new Stack<>();
                newPath.addAll(idxPath.get(minIdx));
                newPath.push(i);
                newPaths.put(i, newPath);
            }
            idxPath = newPaths;
            idxSum = newSums;
        }
        int minIdx = 1;
        for (int i = 2; i < _pic.length; i++) {
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
    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
    }

}
