package bst;

public class BSTDemo {

    public static void main(String[] args) {

        BST agac = new BST();

        // EKLEME
        agac.ekle(10);
        agac.ekle(5);
        agac.ekle(15);
        agac.ekle(3);
        agac.ekle(7);
        agac.ekle(12);
        agac.ekle(20);

        // KÖK
        agac.kokGoster();

        // DOLAŞIMLAR
        System.out.print("Inorder   : ");
        agac.inorderYazdir();

        System.out.print("Preorder  : ");
        agac.preorderYazdir();

        System.out.print("Postorder : ");
        agac.postorderYazdir();

        // ARAMA
        System.out.println("7 var mi? " + agac.ara(7));
        System.out.println("100 var mi? " + agac.ara(100));

        // MIN / MAX
        System.out.println("Min = " + agac.minBul());
        System.out.println("Max = " + agac.maxBul());

        // YÜKSEKLİK
        System.out.println("Yukseklik = " + agac.yukseklik());

        // BFS
        System.out.print("BFS: ");
        agac.bfsYazdir();
    }
}
