import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    int openSites;
    private WeightedQuickUnionUF unionUF;
    private boolean[][] grid;
    private int size;
    private int virtualTopSite;
    private int virtualBottomSite;
    private WeightedQuickUnionUF topVirtualConnect;
    private int N;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N has to be greater than 0");
        }
        grid = new boolean[N][N];
        openSites = 0;
        unionUF = new WeightedQuickUnionUF(N * N + 2);
        size = N;
        virtualBottomSite = N * N + 1;
        virtualTopSite = N * N;
        topVirtualConnect = new WeightedQuickUnionUF(N * N + 1);

        for (int i = 0; i < N; i++) {
            topVirtualConnect.union(i, virtualTopSite);
            unionUF.union(i, virtualTopSite);
        }
    }

    public void open(int row, int col) {
        boundChecker(row, col);
        int newIndex = coordinateChanger(row, col);
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openSites++;
            if (row == 0) {
                unionUF.union(virtualTopSite, newIndex);
            }
            if (row == size - 1) {
                unionUF.union(virtualBottomSite, newIndex);
            }
            if (row - 1 >= 0 && grid[row - 1][col]) {
                unionUF.union(newIndex, coordinateChanger(row - 1, col));
                topVirtualConnect.union(newIndex, coordinateChanger(row - 1, col));
            }
            if (row + 1 < size && grid[row + 1][col]) {
                unionUF.union(newIndex, coordinateChanger(row + 1, col));
                topVirtualConnect.union(newIndex, coordinateChanger(row + 1, col));
            }

            if (col + 1 < size && grid[row][col + 1]) {
                unionUF.union(newIndex, coordinateChanger(row, col + 1));
                topVirtualConnect.union(newIndex, coordinateChanger(row, col + 1));
            }

            if (col - 1 >= 0 && grid[row][col - 1]) {
                unionUF.union(newIndex, coordinateChanger(row, col - 1));
                topVirtualConnect.union(newIndex, coordinateChanger(row, col - 1));
            }

        }
    }

    public boolean isOpen(int row, int col) {
        boundChecker(row, col);
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        boundChecker(row, col);
        int newIndex = coordinateChanger(row, col);
        return (isOpen(row, col) && topVirtualConnect.connected(virtualTopSite, newIndex));
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        if (size == 1) {
            return isOpen(0, 0);
        }
        return unionUF.connected(virtualTopSite, virtualBottomSite);
    }

    public int coordinateChanger(int row, int col) {
        return (row * size) + col;
    }

    public void boundChecker(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IndexOutOfBoundsException("Row and Column size are out of bounds");
        }
    }

}


