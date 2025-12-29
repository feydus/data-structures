package avl;

public class AvlAgaci { // Tek dosyada çalışsın diye sınıfı public yaptık

    // =========================
    // 1) AVL DÜĞÜM SINIFI
    // =========================
    static class Dugum { // Ağacın her elemanı için düğüm yapısı
        int deger;       // Düğümün tuttuğu sayı (anahtar)
        int yukseklik;   // Bu düğümün yüksekliği (AVL denge hesabı için)
        Dugum sol;       // Sol çocuk referansı
        Dugum sag;       // Sağ çocuk referansı

        Dugum(int deger) {          // Yeni düğüm oluştururken çalışır (constructor)
            this.deger = deger;     // Gelen değeri düğümün içine koy
            this.yukseklik = 0;     // Yaprak düğüm yüksekliği: 0 (null=-1 tanımıyla uyumlu)
            this.sol = null;        // Başlangıçta sol çocuk yok
            this.sag = null;        // Başlangıçta sağ çocuk yok
        }
    }

    // =========================
    // 2) AĞACIN KÖKÜ
    // =========================
    private Dugum kok; // AVL ağacının kök düğümü (root)

    public AvlAgaci() { // Boş AVL ağacı oluştur
        this.kok = null; // Başlangıçta kök yok (ağaç boş)
    }

    // =========================
    // 3) YARDIMCI FONKSİYONLAR
    // =========================

    private int yukseklik(Dugum d) { // C'deki yukseklik(P) fonksiyonunun Java karşılığı
        if (d == null) return -1;    // Null ise -1 döndür (boş ağacın yüksekliği)
        return d.yukseklik;          // Değilse düğümün yükseklik alanını döndür
    }

    private int max(int a, int b) {  // İki sayıdan büyüğünü döndüren yardımcı fonksiyon
        return (a > b) ? a : b;      // a büyükse a, değilse b
    }

    private int dengeFaktoru(Dugum d) { // AVL denge faktörü: sol yükseklik - sağ yükseklik
        if (d == null) return 0;         // Null düğüm için denge 0 kabul edilir
        return yukseklik(d.sol) - yukseklik(d.sag); // Sol yükseklik eksi sağ yükseklik
    }

    // =========================
    // 4) DIŞARIDAN ÇAĞRILAN EKLEME
    // =========================
    public void ekle(int deger) {        // Kullanıcı main'den bunu çağırır
        kok = ekleRec(kok, deger);       // Kökten başlayarak recursive ekleme yap, yeni kökü güncelle
    }

    // =========================
    // 5) RECURSIVE EKLEME (BST + AVL DENGELEME)
    // =========================
    private Dugum ekleRec(Dugum T, int x) { // T: bulunduğumuz kök, x: eklenecek değer

        // 5.1) Boş yere geldiysek yeni düğüm oluştur
        if (T == null) return new Dugum(x); // BST ekleme: uygun boş yere yerleştir

        // 5.2) BST kuralı: küçükse sola, büyükse sağa
        if (x < T.deger) {                     // Eklenecek değer küçükse
            T.sol = ekleRec(T.sol, x);         // Sol alt ağaçta eklemeye devam et
        } else if (x > T.deger) {              // Eklenecek değer büyükse
            T.sag = ekleRec(T.sag, x);         // Sağ alt ağaçta eklemeye devam et
        } else {
            // Aynı değer geldiyse (duplicate) eklemiyoruz
            return T;                          // Ağacı değiştirme, olduğu gibi döndür
        }

        // 5.3) Önce yükseklik güncelle (AVL’de her dönüşte yapılmalı)
        T.yukseklik = 1 + max(yukseklik(T.sol), yukseklik(T.sag)); // 1 + max(sol, sağ)

        // 5.4) Denge kontrolü yap
        int denge = dengeFaktoru(T);           // Bu düğümde denge bozuldu mu?

        // =========================
        // 6) 4 AVL DURUMU (LL, RR, LR, RL)
        // =========================

        // 6.1) LL (Sol-Sol): denge > 1 ve x < T.sol.deger ise SAĞA döndür
        if (denge > 1 && x < T.sol.deger) {    // Sol taraf ağır ve ekleme sol-sol olduysa
            return sagaDondur(T);              // Tek sağ dönüş (rotateRight)
        }

        // 6.2) RR (Sağ-Sağ): denge < -1 ve x > T.sag.deger ise SOLA döndür
        if (denge < -1 && x > T.sag.deger) {   // Sağ taraf ağır ve ekleme sağ-sağ olduysa
            return solaDondur(T);              // Tek sol dönüş (rotateLeft)
        }

        // 6.3) LR (Sol-Sağ): denge > 1 ve x > T.sol.deger ise önce SOLA, sonra SAĞA
        if (denge > 1 && x > T.sol.deger) {    // Sol ağır ama ekleme solun sağına geldiyse
            T.sol = solaDondur(T.sol);         // Önce sol çocuğu sola döndür (rotateLeft)
            return sagaDondur(T);              // Sonra kendini sağa döndür (rotateRight)
        }

        // 6.4) RL (Sağ-Sol): denge < -1 ve x < T.sag.deger ise önce SAĞA, sonra SOLA
        if (denge < -1 && x < T.sag.deger) {   // Sağ ağır ama ekleme sağın soluna geldiyse
            T.sag = sagaDondur(T.sag);         // Önce sağ çocuğu sağa döndür (rotateRight)
            return solaDondur(T);              // Sonra kendini sola döndür (rotateLeft)
        }

        // 6.5) Denge bozulmadıysa olduğu gibi döndür
        return T;                               // Bu düğümde rotasyon gerekmiyor
    }

    // =========================
    // 7) TEK DÖNDÜRME: SAĞA DÖNDÜR (LL DÜZELTMESİ)
    // =========================
    private Dugum sagaDondur(Dugum k2) {       // k2: dönüş yapılacak düğüm
        Dugum k1 = k2.sol;                      // k1, k2'nin sol çocuğu (yeni kök olacak)
        Dugum k1inSagi = k1.sag;                // k1'in sağ alt ağacını yedekle (kayıp olmasın)

        // Döndürme bağlantıları
        k1.sag = k2;                            // k2 aşağı iner, k1'in sağı olur
        k2.sol = k1inSagi;                      // k1'in sağ alt ağacı, k2'nin solu olur

        // Yükseklik güncelleme (önce aşağı inen düğüm)
        k2.yukseklik = 1 + max(yukseklik(k2.sol), yukseklik(k2.sag)); // k2'nin yeni yüksekliği
        k1.yukseklik = 1 + max(yukseklik(k1.sol), yukseklik(k1.sag)); // k1'in yeni yüksekliği

        return k1;                               // Yeni kökü döndür
    }

    // =========================
    // 8) TEK DÖNDÜRME: SOLA DÖNDÜR (RR DÜZELTMESİ)
    // =========================
    private Dugum solaDondur(Dugum k2) {        // k2: dönüş yapılacak düğüm
        Dugum k1 = k2.sag;                       // k1, k2'nin sağ çocuğu (yeni kök olacak)
        Dugum k1inSolu = k1.sol;                 // k1'in sol alt ağacını yedekle

        // Döndürme bağlantıları
        k1.sol = k2;                             // k2 aşağı iner, k1'in solu olur
        k2.sag = k1inSolu;                       // k1'in sol alt ağacı, k2'nin sağı olur

        // Yükseklik güncelleme
        k2.yukseklik = 1 + max(yukseklik(k2.sol), yukseklik(k2.sag)); // k2'nin yeni yüksekliği
        k1.yukseklik = 1 + max(yukseklik(k1.sol), yukseklik(k1.sag)); // k1'in yeni yüksekliği

        return k1;                               // Yeni kökü döndür
    }

    // =========================
    // 9) DOLAŞMA: INORDER (SIRALI YAZDIRIR)
    // =========================
    public void inorderYazdir() {                // Dışarıdan çağrılan yazdırma
        inorderRec(kok);                         // Kökten inorder dolaş
        System.out.println();                    // Satır sonu
    }

    private void inorderRec(Dugum d) {           // Inorder recursive fonksiyonu
        if (d == null) return;                   // Boşsa geri dön
        inorderRec(d.sol);                       // Sol alt ağaç
        System.out.print(d.deger + " ");         // Düğümü yazdır
        inorderRec(d.sag);                       // Sağ alt ağaç
    }

    // =========================
    // 10) ARAMA (BST ARAMA)
    // =========================
    public boolean ara(int x) {                  // Dışarıdan çağır: ağaçta var mı?
        return araRec(kok, x);                   // Kökten recursive ara
    }

    private boolean araRec(Dugum d, int x) {     // d: bulunduğun düğüm
        if (d == null) return false;             // Boşa düştüysek yok demektir
        if (x == d.deger) return true;           // Eşitse bulundu
        if (x < d.deger) return araRec(d.sol, x);// Küçükse sola git
        return araRec(d.sag, x);                 // Büyükse sağa git
    }

    // =========================
    // 11) YÜKSEKLİK SORGUSU (KÖK YÜKSEKLİĞİ)
    // =========================
    public int agacYuksekligi() {                // Ağacın yüksekliğini dışarıdan öğren
        return yukseklik(kok);                   // Kökün yüksekliği ağacın yüksekliğidir
    }

    // =========================
    // 12) MAIN: TÜM FONKSİYONLARI KULLANMA
    // =========================
    public static void main(String[] args) {     // Programın başlangıç noktası

        AvlAgaci avl = new AvlAgaci();           // Boş AVL ağacı oluştur

        // 12.1) Ekleme testleri (rotasyonlar otomatik çalışır)
        int[] sayilar = { 10, 20, 30, 40, 50, 25 }; // Örnek veri seti (klasik AVL örneği)
        for (int x : sayilar) {                  // Her elemanı sırayla dolaş
            avl.ekle(x);                         // AVL'ye ekle (denge bozulursa döndürmeler içeride olur)
        }

        // 12.2) Inorder yazdır (sıralı gelmeli)
        System.out.print("Inorder (sirali): ");  // Başlık yaz
        avl.inorderYazdir();                     // 10 20 25 30 40 50 beklenir

        // 12.3) Arama örnekleri
        int aranan1 = 25;                        // Ağaçta olan bir değer
        int aranan2 = 99;                        // Ağaçta olmayan bir değer
        System.out.println(aranan1 + " var mi? " + avl.ara(aranan1)); // true beklenir
        System.out.println(aranan2 + " var mi? " + avl.ara(aranan2)); // false beklenir

        // 12.4) Ağacın yüksekliğini yazdır
        System.out.println("Agac yuksekligi: " + avl.agacYuksekligi()); // AVL dengeli olduğu için küçük çıkar
    }
}

