/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdventDay19 {
    private String[] file;
    private String[] monster = {
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   "
    };

    public class Tile {
        private long id;
        private int tileSize;
        private int currTileInd;
        private ArrayList<ArrayList<String>> rotatedTiles;
        private boolean checked;

        public Tile(int id, ArrayList<String> tile) {
            this.id = id;
            this.tileSize = tile.size();
            this.rotatedTiles = rotateTile(tile);
            this.currTileInd = 0;
            this.checked = false;
        }

        public boolean isChecked() {
            return this.checked;
        }

        public void unCheck() {
            this.checked = !this.checked;
        }

        public void rotate() {
            this.currTileInd = (this.currTileInd + 1) % this.rotatedTiles.size();
        }

        public ArrayList<String> getTile() {
            return this.rotatedTiles.get(this.currTileInd);
        }

        private ArrayList<String> createTile(ArrayList<String> tile, int fDir, int secDir,
                                             int thirdDir) {
            ArrayList<String> a = new ArrayList<String>();
            int i = fDir > 0 ? 0 : tile.size() - 1;

            while (i >= 0 && i < tile.size()) {
                String str = "";
                int j = secDir > 0 ? 0 : tile.size() - 1;
                while (j >= 0 && j < tile.size()) {
                    str += (thirdDir > 0) ? tile.get(i).charAt(j) : tile.get(j).charAt(i);
                    j = j + secDir;
                }
                a.add(str);
                i = i + fDir;
            }
            return a;
        }

        private ArrayList<ArrayList<String>> rotateTile(ArrayList<String> tile) {
            ArrayList<ArrayList<String>> r = new ArrayList<ArrayList<String>>();
            for (int i = -1; i <= 1; i = i + 2) {
                for (int j = -1; j <= 1; j = j + 2) {
                    for (int k = -1; k <= 1; k = k + 2) {
                        r.add(createTile(tile, i, j, k));
                    }
                }
            }
            return r;
        }

        public long getID() {
            return this.id;
        }

        public int numOfRotations() {
            return rotatedTiles.size();
        }

        public int getTileSize() {
            return this.tileSize;
        }

        public char getSymbol(int i, int j) {
            return rotatedTiles.get(currTileInd).get(i).charAt(j);
        }

        public String getString(int i) {
            return rotatedTiles.get(currTileInd).get(i);
        }
    }

    public AdventDay19(String[] f) {
        this.file = f;
    }


    private ArrayList<Tile> parseTiles() {
        ArrayList<Tile> g = new ArrayList<Tile>();
        ArrayList<String> temp = new ArrayList<String>();
        int currID = 0;
        int i = 0;

        while (i < this.file.length) {
            String s = this.file[i];
            if (s.contains("Tile")) {
                currID = Integer.parseInt(s.split(" |:")[1]);
            }
            else if (s.isEmpty() || i == this.file.length - 1) {
                if (i == this.file.length - 1) temp.add(s);
                Tile t = new Tile(currID, temp);
                g.add(t);
                temp = new ArrayList<String>();
            }
            else {
                temp.add(s);
            }
            i++;
        }
        return g;
    }

    private boolean compatible(Tile tile, Tile[][] img, int i, int j) {
        if (j >= 1) {
            Tile comp = img[i][j - 1];
            for (int k = 0; k < tile.getTileSize(); k++) {
                if (comp.getSymbol(k, comp.getTileSize() - 1) != tile.getSymbol(k, 0)) return false;
            }
        }

        if (i >= 1) {
            Tile comp = img[i - 1][j];
            for (int k = 0; k < tile.getTileSize(); k++) {
                if (comp.getSymbol(comp.getTileSize() - 1, k) != tile.getSymbol(0, k)) return false;
            }
        }

        return true;
    }

    private boolean generateIMG(Tile[][] image, ArrayList<Tile> tiles,
                                int pos) {
        if (pos >= image.length * image.length)
            return true;

        int i = pos / image.length;
        int j = pos % image.length;

        for (int k = 0; k < tiles.size(); k++) {
            Tile curr = tiles.get(k);
            if (curr.isChecked()) continue;
            for (int h = 0; h < curr.numOfRotations(); h++) {
                curr.rotate();
                if (compatible(curr, image, i, j)) {
                    image[i][j] = curr;
                    if (!curr.isChecked()) curr.unCheck();
                    if (generateIMG(image, tiles, pos + 1)) {
                        return true;
                    }
                }
            }
            if (curr.isChecked()) curr.unCheck();
        }

        return false;
    }

    private Tile createIMG(Tile[][] image) {
        ArrayList<String> a = new ArrayList<String>();
        for (int i = 0; i < image.length; i++) {
            for (int k = 1; k < image[i][0].getTileSize() - 1; k++) {
                String s = "";
                for (int j = 0; j < image.length; j++) {
                    String tileStr = image[i][j].getString(k);
                    s += tileStr.substring(1, tileStr.length() - 1);
                }
                a.add(s);
            }
        }
        return new Tile(1, a);
    }

    public String replaceChar(String str, char ch, int index) {
        char[] chars = str.toCharArray();
        chars[index] = ch;
        return String.valueOf(chars);
    }

    private int checkMonster(Tile t, int monstersBarNum) {
        Set<Integer> checkedPos = new HashSet<>();
        int size = t.getTileSize();
        int start = 0;
        int barNum;
        int res = 0;

        while (start < (size * size - this.monster.length * this.monster[0].length())) {
            int i = start / size;
            int j = start % size;
            Set<Integer> tempSet = new HashSet<>();
            barNum = 0;

            outerloop:
            for (int k = 0; k < this.monster[0].length(); k++) {
                for (int h = 0; h < this.monster.length; h++) {
                    if (monster[h].charAt(k) == ' ') continue;
                    if ((i + h) < size && (j + k) < size && monster[h].charAt(k) == t
                            .getSymbol(i + h, j + k)) {
                        barNum++;
                        tempSet.add((i + h) * size + j + k);
                    }

                    else break outerloop;
                }
            }

            if (barNum == monstersBarNum) {
                res += barNum;
                for (int pos : tempSet) { // check if we counted this position before in case of intersections
                    if (checkedPos.contains(pos))
                        res--;
                    else checkedPos.add(pos);
                }
            }

            if (j + this.monster[0].length() == size) // end of the line
                start += this.monster[0].length();  // jump to next line
            else
                start++;
        }
        return res;
    }

    private int findMonsters(Tile t, int monstersBarNum) {
        int res = 0;
        for (int i = 0; i < t.numOfRotations(); i++) {
            if (res == 0) {
                t.rotate();
                res = checkMonster(t, monstersBarNum);
            }
            else break;
        }
        return res;
    }

    private int findBars(List<String> a) {
        int res = 0;
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.get(i).length(); j++) {
                if (a.get(i).charAt(j) == '#')
                    res++;
            }
        }
        return res;
    }

    private long countIDs(Tile[][] image) {
        int sizeOfImg = image.length;
        return image[0][0].getID() * image[sizeOfImg - 1][0].getID() * image[0][sizeOfImg - 1]
                .getID() * image[sizeOfImg - 1][sizeOfImg - 1].getID();
    }

    private int barsLeft(Tile resultTile) {
        int mostersBarNum = findBars(Arrays.asList(this.monster));
        int tilesBarNum = findBars(resultTile.getTile());

        return tilesBarNum - findMonsters(resultTile, mostersBarNum);
    }

    public void getResults() {
        ArrayList<Tile> tiles = parseTiles();
        int sizeOfImg = (int) Math.round(Math.sqrt(tiles.size()));
        Tile[][] image = new Tile[sizeOfImg][sizeOfImg];
        generateIMG(image, tiles, 0);

        Tile resultTile = createIMG(image);

        StdOut.println(countIDs(image));
        StdOut.println(barsLeft(resultTile));
    }

    public static void main(String[] args) {
        AdventDay19 a = new AdventDay19(StdIn.readAllLines());
        a.getResults();
    }
}
