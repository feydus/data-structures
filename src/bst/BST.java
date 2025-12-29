package bst;

import java.util.LinkedList;
import java.util.Queue;

public class BST {

    private Dugum root;

    public BST() {
        root = null;
    }

    // =========================
    // EKLEME
    // =========================
    public void ekle(int veri) {
        root = ekleRec(root, veri);
    }

    private Dugum ekleRec(Dugum dugum, int veri) {
        if (dugum == null) return new Dugum(veri);

        if (veri < dugum.veri) dugum.sol = ekleRec(dugum.sol, veri);
        else if (veri > dugum.veri) dugum.sag = ekleRec(dugum.sag, veri);
        // eşitse ekleme (tekrarlı değer istemiyorsan)
        return dugum;
    }

    // =========================
    // KÖK GÖSTER
    // =========================
    public void kokGoster() {
        if (root == null) {
            System.out.println("Kök: (ağaç boş)");
        } else {
            System.out.println("Kök: " + root.veri);
        }
    }

    // =========================
    // DOLAŞIMLAR (TRAVERSAL)
    // =========================
    public void inorderYazdir() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(Dugum dugum) {
        if (dugum == null) return;
        inorderRec(dugum.sol);
        System.out.print(dugum.veri + " ");
        inorderRec(dugum.sag);
    }

    public void preorderYazdir() {
        preorderRec(root);
        System.out.println();
    }

    private void preorderRec(Dugum dugum) {
        if (dugum == null) return;
        System.out.print(dugum.veri + " ");
        preorderRec(dugum.sol);
        preorderRec(dugum.sag);
    }

    public void postorderYazdir() {
        postorderRec(root);
        System.out.println();
    }

    private void postorderRec(Dugum dugum) {
        if (dugum == null) return;
        postorderRec(dugum.sol);
        postorderRec(dugum.sag);
        System.out.print(dugum.veri + " ");
    }

    // =========================
    // ARAMA
    // =========================
    public boolean ara(int veri) {
        return araRec(root, veri);
    }

    private boolean araRec(Dugum dugum, int veri) {
        if (dugum == null) return false;
        if (veri == dugum.veri) return true;
        if (veri < dugum.veri) return araRec(dugum.sol, veri);
        return araRec(dugum.sag, veri);
    }

    // =========================
    // MIN / MAX
    // =========================
    public int minBul() {
        if (root == null) throw new IllegalStateException("Ağaç boş!");
        Dugum cur = root;
        while (cur.sol != null) cur = cur.sol;
        return cur.veri;
    }

    public int maxBul() {
        if (root == null) throw new IllegalStateException("Ağaç boş!");
        Dugum cur = root;
        while (cur.sag != null) cur = cur.sag;
        return cur.veri;
    }

    // =========================
    // YÜKSEKLİK
    // Boş ağaç: -1 (kenar sayısı tanımı)
    // =========================
    public int yukseklik() {
        return yukseklikRec(root);
    }

    private int yukseklikRec(Dugum dugum) {
        if (dugum == null) return -1;
        int sol = yukseklikRec(dugum.sol);
        int sag = yukseklikRec(dugum.sag);
        return 1 + Math.max(sol, sag);
    }

    // =========================
    // BFS (SEVİYE SEVİYE)
    // =========================
    public void bfsYazdir() {
        if (root == null) {
            System.out.println("(ağaç boş)");
            return;
        }

        Queue<Dugum> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            Dugum cur = q.poll();
            System.out.print(cur.veri + " ");

            if (cur.sol != null) q.add(cur.sol);
            if (cur.sag != null) q.add(cur.sag);
        }
        System.out.println();
    }

    // =========================
    // BELİRLİ SEVİYEYİ YAZDIR
    // seviye=0 -> kök
    // =========================
    public void seviyeYazdir(int hedefSeviye) {
        if (hedefSeviye < 0) {
            System.out.println("(geçersiz seviye)");
            return;
        }
        seviyeRec(root, 0, hedefSeviye);
        System.out.println();
    }

    private void seviyeRec(Dugum dugum, int mevcut, int hedef) {
        if (dugum == null) return;
        if (mevcut == hedef) {
            System.out.print(dugum.veri + " ");
            return;
        }
        seviyeRec(dugum.sol, mevcut + 1, hedef);
        seviyeRec(dugum.sag, mevcut + 1, hedef);
    }

    // =========================
    // SİLME
    // =========================
    public void sil(int veri) {
        root = silRec(root, veri);
    }

    private Dugum silRec(Dugum dugum, int veri) {
        if (dugum == null) return null;

        if (veri < dugum.veri) {
            dugum.sol = silRec(dugum.sol, veri);
        } else if (veri > dugum.veri) {
            dugum.sag = silRec(dugum.sag, veri);
        } else {
            // 1) çocuk yok
            if (dugum.sol == null && dugum.sag == null) {
                return null;
            }
            // 2) tek çocuk
            if (dugum.sol == null) return dugum.sag;
            if (dugum.sag == null) return dugum.sol;

            // 3) iki çocuk:
            // sağ alt ağacın en küçüğünü (inorder successor) bul
            Dugum successor = minDugum(dugum.sag);
            dugum.veri = successor.veri;
            // successor'ı sağ alt ağaçtan sil
            dugum.sag = silRec(dugum.sag, successor.veri);
        }

        return dugum;
    }

    private Dugum minDugum(Dugum dugum) {
        Dugum cur = dugum;
        while (cur != null && cur.sol != null) cur = cur.sol;
        return cur;
    }

    // =========================
    // TERSİNE AL (MIRROR)
    // =========================
    public void tersiniAl() {
        root = mirrorRec(root);
    }

    private Dugum mirrorRec(Dugum dugum) {
        if (dugum == null) return null;

        Dugum sol = mirrorRec(dugum.sol);
        Dugum sag = mirrorRec(dugum.sag);

        dugum.sol = sag;
        dugum.sag = sol;

        return dugum;
    }
}
