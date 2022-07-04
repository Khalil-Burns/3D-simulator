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
    public static mPoint vecAdd(mPoint v1, mPoint v2) {
        return(new mPoint(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z));
    }
    public static mPoint vecSub(mPoint v1, mPoint v2) {
        return(new mPoint(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z));
    }
    public static mPoint multiply(mPoint v, double scaler) {
        return(new mPoint(v.x * scaler, v.y * scaler, v.z * scaler));
    }
    public static mPoint divide(mPoint v, double scaler) {
        return(new mPoint(v.x / scaler, v.y / scaler, v.z / scaler));
    }
    public static double dotProduct(mPoint v1, mPoint v2) {
        return(v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
    }
    public static double vecLength(mPoint v) {
        return(Math.sqrt(dotProduct(v, v)));
    }
    public static mPoint vecNormalise(mPoint v) {
        double l = vecLength(v);
        return(new mPoint(v.x / l, v.y / l, v.z / l));
    }
    public static mPoint crossProduct(mPoint v1, mPoint v2) {
        mPoint output = new mPoint();
        output.x = v1.y * v2.z - v1.z * v2.y;
		output.y = v1.z * v2.x - v1.x * v2.z;
		output.z = v1.x * v2.y - v1.y * v2.x;
        return(output);
    }
    public static mPoint intersectVectors(mPoint point, mPoint pNorm, mPoint lineStart, mPoint lineEnd, double[] _t) {
        pNorm = vecNormalise(pNorm);
        double planeD = dotProduct(pNorm, point);
        double ad = dotProduct(lineStart, pNorm);
        double bd = dotProduct(lineEnd, pNorm);
        double t = (planeD - ad) / (bd - ad);
        _t[0] = t;
        mPoint startToEnd = vecSub(lineEnd, lineStart);
        mPoint lineToIntersect = multiply(startToEnd, t);
        return(vecAdd(lineStart, lineToIntersect));
    }

    public static double distance(mPoint p, mPoint pPoint, mPoint pNorm) {
		return (pNorm.x * p.x + pNorm.y * p.y + pNorm.z * p.z - dotProduct(pNorm, pPoint));
    }

    public static mPoint transformPoint(mPoint input) {
        projMatrix[0][0] = (Main.fAspectRatio * Main.fFovRad);
        projMatrix[1][1] = (Main.fFovRad);
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