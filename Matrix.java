public class Matrix {
    double[][] matrix = new double[4][4];
    static double[][] projMatrix = {
        {(Main.fAspectRatio * Main.fFovRad), 0             , 0                                                     , 0},
        {0                                 , (Main.fFovRad), 0                                                     , 0},
        {0                                 , 0             , (Main.fFar / (Main.fFar - Main.fNear))                , 1},
        {0                                 , 0             , ((-Main.fFar * Main.fNear) / (Main.fFar - Main.fNear)), 0}
    };

    public mPoint MultiplyMatrixVector(mPoint input) {
        double w = input.x * this.matrix[0][3] + input.y * this.matrix[1][3] + input.z * this.matrix[2][3] + this.matrix[3][3];
        if (w == 0) {
            w = 1;
        }
        mPoint output = new mPoint(
            (input.x * this.matrix[0][0] + input.y * this.matrix[1][0] + input.z * this.matrix[2][0] + this.matrix[3][0]) / (double)w,
            (input.x * this.matrix[0][1] + input.y * this.matrix[1][1] + input.z * this.matrix[2][1] + this.matrix[3][1]) / (double)w,
            (input.x * this.matrix[0][2] + input.y * this.matrix[1][2] + input.z * this.matrix[2][2] + this.matrix[3][2]) / (double)w
        );
        return(output);
    }
    public static mPoint vecSub(mPoint v1, mPoint v2) {
        return(new mPoint(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z));
    }
    public static double dotProduct(mPoint v1, mPoint v2) {
        return(v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
    }
    public static mPoint transformPoint(mPoint input) {
        double w = input.x * projMatrix[0][3] + input.y * projMatrix[1][3] + input.z * projMatrix[2][3] + projMatrix[3][3];
        if (w == 0) {
            w = 1;
        }
        mPoint output = new mPoint(
            (input.x * projMatrix[0][0] + input.y * projMatrix[1][0] + input.z * projMatrix[2][0] + projMatrix[3][0]) / (double)w,
            (input.x * projMatrix[0][1] + input.y * projMatrix[1][1] + input.z * projMatrix[2][1] + projMatrix[3][1]) / (double)w,
            (input.x * projMatrix[0][2] + input.y * projMatrix[1][2] + input.z * projMatrix[2][2] + projMatrix[3][2]) / (double)w
        );
        return(output);
    }
}