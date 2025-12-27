import java.util.LinkedList;                 // BFS için Queue altyapısı
import java.util.Queue;                      // Seviye işlemleri için Queue

// -------------------------
// DÜĞÜM (NODE) SINIFI
// -------------------------
class Dugum {                                // Ağaçtaki her elemanı temsil eder
    int veri;                                // Düğümün tuttuğu değer
    Dugum sol;                               // Sol çocuk referansı
    Dugum sag;                               // Sağ çocuk referansı

    Dugum(int veri) {                        // Kurucu metot: düğüm oluşturma
        this.veri = veri;                    // Gelen değeri düğüme ata
        this.sol = null;                     // Sol çocuk başlangıçta yok
        this.sag = null;                     // Sağ çocuk başlangıçta yok
    }
}

// -------------------------
// BST (İKİLİ ARAMA AĞACI) SINIFI
// -------------------------
class BST {                                  // BST işlemlerinin tamamı burada
    Dugum root;                              // Ağacın kökü

    BST() {                                  // BST kurucu metodu
        root = null;                         // Ağaç başlangıçta boş
    }

    // ==========================================================
    // 0) KÖK GÖSTER (Senin istediğin 1. madde)
    // ==========================================================
    public void kokGoster() {                // Kök değerini ekrana yazar
        if (root != null)                    // Kök varsa
            System.out.println("Kok degeri = " + root.veri); // Kök değerini yaz
        else                                 // Kök yoksa
            System.out.println("Kok yok, Agac Bos!");        // Ağaç boş mesajı ver
    }

    // ==========================================================
    // 1) EKLEME (INSERT) - Senin mevcut kodun
    // ==========================================================
    void ekle(int veri) {                    // Kullanıcının çağırdığı ekleme metodu
        root = ekleRec(root, veri);          // Recursive ekleme kökten başlar ve kök güncellenir
    }                                        // root= yazma sebebi: ağaç boş olabilir / bağlar güncellenebilir

    Dugum ekleRec(Dugum mevcut, int veri) {  // Ağaç içinde uygun yeri bulup ekler
        if (mevcut == null) {                // Boş yere geldiysek
            return new Dugum(veri);          // Yeni düğüm oluştur ve geri dön
        }

        if (veri < mevcut.veri) {            // Eklenecek değer küçükse
            mevcut.sol = ekleRec(mevcut.sol, veri); // Sol alt ağaca git ve ekle
        } else if (veri > mevcut.veri) {     // Eklenecek değer büyükse
            mevcut.sag = ekleRec(mevcut.sag, veri); // Sağ alt ağaca git ve ekle
        }                                    // Eşitse ekleme yok (tekrarlı veri kabul etmiyoruz)

        return mevcut;                       // Mevcut düğümü geri döndür (bağlantıları korur)
    }

    // ==========================================================
    // 2) ARAMA (SEARCH) - Senin mevcut kodun
    // ==========================================================
    boolean ara(int aranan) {                // Dışarıdan çağırılan arama
        return araRec(root, aranan);         // Recursive aramayı kökten başlat
    }

    boolean araRec(Dugum mevcut, int aranan) { // Recursive arama
        if (mevcut == null) {                // Düğüm yoksa
            return false;                    // Bulunamadı
        }
        if (aranan == mevcut.veri) {         // Aranan değer bulunduysa
            return true;                     // Bulundu
        }
        if (aranan < mevcut.veri) {          // Aranan küçükse
            return araRec(mevcut.sol, aranan); // Sol alt ağaçta ara
        } else {                             // Aranan büyükse
            return araRec(mevcut.sag, aranan); // Sağ alt ağaçta ara
        }
    }

    // ==========================================================
    // 3) SİLME (DELETE) - Senin mevcut kodun
    // ==========================================================
    void sil(int veri) {                     // Dışarıdan çağırılan silme
        root = silRec(root, veri);           // Silme sonrası kök değişebilir, root güncellenir
    }

    Dugum silRec(Dugum mevcut, int veri) {   // Recursive silme
        if (mevcut == null) {                // Düğüm yoksa
            return null;                     // Silinecek değer bulunamadı
        }

        if (veri < mevcut.veri) {            // Silinecek değer küçükse
            mevcut.sol = silRec(mevcut.sol, veri); // Sol alt ağaçta sil
        } else if (veri > mevcut.veri) {     // Silinecek değer büyükse
            mevcut.sag = silRec(mevcut.sag, veri); // Sağ alt ağaçta sil
        } else {                             // Düğümü bulduk

            if (mevcut.sol == null && mevcut.sag == null) { // Durum 1: yaprak
                return null;                 // Yaprağı sil
            }

            if (mevcut.sol == null) {        // Durum 2: tek çocuk (sağ)
                return mevcut.sag;           // Sağı yukarı al
            }

            if (mevcut.sag == null) {        // Durum 2: tek çocuk (sol)
                return mevcut.sol;           // Solu yukarı al
            }

            Dugum minSag = minBulDugum(mevcut.sag);         // Durum 3: sağ alt ağacın min düğümü
            mevcut.veri = minSag.veri;                      // Değeri kopyala (successor)
            mevcut.sag = silRec(mevcut.sag, minSag.veri);   // Aşağıdaki min düğümü sil
        }

        return mevcut;                       // Güncellenmiş düğümü geri döndür
    }

    // ==========================================================
    // 4) INORDER / PREORDER / POSTORDER (Senin istediğin traversal'lar)
    // ==========================================================
    void inorderYazdir() {                   // Dışarıdan inorder çağrısı
        inorderRec(root);                    // Kökten inorder gez
        System.out.println();                // Satır sonu
    }

    void inorderRec(Dugum d) {               // Inorder: L N R
        if (d == null) return;               // Düğüm yoksa dur
        inorderRec(d.sol);                   // Solu gez
        System.out.print(d.veri + " ");      // Kökü yaz
        inorderRec(d.sag);                   // Sağı gez
    }

    void preorderYazdir() {                  // Dışarıdan preorder çağrısı
        preorderRec(root);                   // Kökten preorder gez
        System.out.println();                // Satır sonu
    }

    void preorderRec(Dugum d) {              // Preorder: N L R
        if (d == null) return;               // Düğüm yoksa dur
        System.out.print(d.veri + " ");      // Önce kökü yaz
        preorderRec(d.sol);                  // Sonra sol
        preorderRec(d.sag);                  // Sonra sağ
    }

    void postorderYazdir() {                 // Dışarıdan postorder çağrısı
        postorderRec(root);                  // Kökten postorder gez
        System.out.println();                // Satır sonu
    }

    void postorderRec(Dugum d) {             // Postorder: L R N
        if (d == null) return;               // Düğüm yoksa dur
        postorderRec(d.sol);                 // Solu gez
        postorderRec(d.sag);                 // Sağı gez
        System.out.print(d.veri + " ");      // En son kökü yaz
    }

    // ==========================================================
    // 5) Yaprakları göster (Senin 2. madde)
    // ==========================================================
    public void yapraklariGoster() {         // Dışarıdan çağırma kolaylığı için
        yapraklariGosterRec(root);           // Kökten başlat
        System.out.println();                // Satır sonu
    }

    public void yapraklariGosterRec(Dugum dugum) {          // Yaprak yazdırma recursive
        if (dugum == null) return;                           // Düğüm yoksa dur
        if (dugum.sol == null && dugum.sag == null)          // Yaprak mı? (çocuk yok)
            System.out.print(dugum.veri + " ");              // Yaprak değerini yaz
        yapraklariGosterRec(dugum.sol);                      // Sol alt ağaçta ara
        yapraklariGosterRec(dugum.sag);                      // Sağ alt ağaçta ara
    }

    // ==========================================================
    // 6) Çocukları göster (Senin 3. madde)
    // ==========================================================
    public void cocuklariGoster() {         // Dışarıdan çağırma kolaylığı için
        cocuklariGosterRec(root);           // Kökten başlat
    }

    public void cocuklariGosterRec(Dugum dugum) {                     // Her düğümün çocuklarını yazdırır
        if (dugum == null) return;                                    // Düğüm yoksa dur

        if (dugum.sol != null)                                        // Sol çocuk varsa
            System.out.println(dugum.veri + " -> Sol cocuk = " + dugum.sol.veri); // Sol çocuğu yaz
        else                                                          // Sol çocuk yoksa
            System.out.println(dugum.veri + " -> Sol cocuk yok!");     // Mesaj yaz

        if (dugum.sag != null)                                        // Sağ çocuk varsa
            System.out.println(dugum.veri + " -> Sag cocuk = " + dugum.sag.veri); // Sağ çocuğu yaz
        else                                                          // Sağ çocuk yoksa
            System.out.println(dugum.veri + " -> Sag cocuk yok!");     // Mesaj yaz

        cocuklariGosterRec(dugum.sol);                                 // Sol alt ağaçta devam et
        cocuklariGosterRec(dugum.sag);                                 // Sağ alt ağaçta devam et
    }

    // ==========================================================
    // 7) Küçükten büyüğe / Büyükten küçüğe sırala (Senin 4. madde)
    // ==========================================================
    public void kucuktenBuyuge() {           // Küçükten büyüğe yazdır
        kucuktenBuyugeRec(root);             // Kökten başlat (inorder)
        System.out.println();                // Satır sonu
    }

    public void kucuktenBuyugeRec(Dugum dugum) { // Inorder ile küçükten büyüğe
        if (dugum == null) return;           // Düğüm yoksa dur
        kucuktenBuyugeRec(dugum.sol);        // Sol
        System.out.print(dugum.veri + " ");  // Kök
        kucuktenBuyugeRec(dugum.sag);        // Sağ
    }

    public void buyuktenKucuge() {           // Büyükten küçüğe yazdır
        buyuktenKucugeRec(root);             // Kökten başlat (reverse inorder)
        System.out.println();                // Satır sonu
    }

    public void buyuktenKucugeRec(Dugum dugum) { // Reverse inorder ile büyükten küçüğe
        if (dugum == null) return;           // Düğüm yoksa dur
        buyuktenKucugeRec(dugum.sag);        // Önce sağ (büyükler)
        System.out.print(dugum.veri + " ");  // Sonra kök
        buyuktenKucugeRec(dugum.sol);        // Sonra sol (küçükler)
    }

    // ==========================================================
    // 8) Min / Max bul (Senin 5. madde)
    // ==========================================================
    public int minBul() {                    // Ağaçtaki minimum değeri döndürür
        if (root == null) throw new IllegalStateException("Agac bos"); // Boş ağaç kontrolü
        return minBulDugum(root).veri;       // Min düğümün değerini döndür
    }

    public int maxBul() {                    // Ağaçtaki maksimum değeri döndürür
        if (root == null) throw new IllegalStateException("Agac bos"); // Boş ağaç kontrolü
        return maxBulDugum(root).veri;       // Max düğümün değerini döndür
    }

    public Dugum minBulDugum(Dugum dugum) {  // Verilen alt ağaçta min düğümü bulur
        while (dugum.sol != null) {          // Sol oldukça devam et
            dugum = dugum.sol;               // Sola git
        }
        return dugum;                        // En soldaki min'dir
    }

    public Dugum maxBulDugum(Dugum dugum) {  // Verilen alt ağaçta max düğümü bulur
        while (dugum.sag != null) {          // Sağ oldukça devam et
            dugum = dugum.sag;               // Sağa git
        }
        return dugum;                        // En sağdaki max'tır
    }

    // ==========================================================
    // 9) Ağacın yüksekliğini bul (Senin 6. madde)
    // ==========================================================
    public int yukseklik() {                 // Dışarıdan çağırma kolaylığı
        return yukseklikRec(root);           // Kökten başlat
    }

    public int yukseklikRec(Dugum dugum) {   // Yükseklik hesaplar
        if (dugum == null) return -1;        // Boş ağacın yüksekliği -1 (0 kenarlı tanım)
        int sol = yukseklikRec(dugum.sol);   // Sol alt ağacın yüksekliği
        int sag = yukseklikRec(dugum.sag);   // Sağ alt ağacın yüksekliği
        return 1 + Math.max(sol, sag);       // Büyük olan +1
    }

    // ==========================================================
    // 10) Tüm elemanları toplama (Senin 7. madde)
    // ==========================================================
    public int toplamElemanlar() {           // Dışarıdan çağırma kolaylığı
        return toplamElemanlarRec(root);     // Kökten başlat
    }

    public int toplamElemanlarRec(Dugum dugum) { // Tüm düğüm değerlerini toplar
        if (dugum == null) return 0;         // Boşsa 0
        return dugum.veri                    // Kök değer
                + toplamElemanlarRec(dugum.sol) // Sol toplam
                + toplamElemanlarRec(dugum.sag); // Sağ toplam
    }

    // ==========================================================
    // 11) Ağacın tersini alma (mirror) (Senin 8. madde)
    // ==========================================================
    public void tersiniAl() {                // Dışarıdan çağırma kolaylığı
        tersiniAlRec(root);                  // Kökten başlat
    }

    public void tersiniAlRec(Dugum dugum) {  // Ayna görüntüsü (sol-sağ değiştir)
        if (dugum == null) return;           // Düğüm yoksa dur

        Dugum temp = dugum.sol;              // Solu geçiciye al
        dugum.sol = dugum.sag;               // Sağı sola koy
        dugum.sag = temp;                    // Eski solu sağa koy

        tersiniAlRec(dugum.sol);             // Yeni sol alt ağaçta devam
        tersiniAlRec(dugum.sag);             // Yeni sağ alt ağaçta devam
    }

    // ==========================================================
    // 12) Düğümün seviyesini bul (Senin 9. madde)
    // ==========================================================
    public int seviyeBul(int aranan) {       // Dışarıdan sadece değer al
        return seviyeBulRec(root, aranan, 0);// Seviye 0'dan başla
    }

    public int seviyeBulRec(Dugum dugum, int aranan, int seviye) { // Recursive seviye bulma
        if (dugum == null) return -1;        // Bulunamadıysa -1
        if (dugum.veri == aranan) return seviye; // Bulunduysa seviyesi

        int sol = seviyeBulRec(dugum.sol, aranan, seviye + 1); // Solda ara
        if (sol != -1) return sol;            // Solda bulunduysa dön

        return seviyeBulRec(dugum.sag, aranan, seviye + 1);     // Sağda ara
    }

    // ==========================================================
    // 13) Belirli seviyedeki tüm elemanları göster (Senin 10. madde)
    // ==========================================================
    public void seviyeYazdir(int hedefSeviye) {       // Dışarıdan seviye alır
        seviyeYazdirRec(root, hedefSeviye);           // Kökten başlat
        System.out.println();                         // Satır sonu
    }

    public void seviyeYazdirRec(Dugum dugum, int seviye) { // Hedef seviyeyi yazdırır
        if (dugum == null) return;                    // Düğüm yoksa dur

        if (seviye == 0) {                            // Hedef seviyeye geldiysek
            System.out.print(dugum.veri + " ");        // Yazdır
            return;                                   // Bu dalda daha aşağı inme
        }

        seviyeYazdirRec(dugum.sol, seviye - 1);       // Sol dalda seviye azalt
        seviyeYazdirRec(dugum.sag, seviye - 1);       // Sağ dalda seviye azalt
    }

    // ==========================================================
    // 14) (BONUS) BFS ile seviye seviye göster (hocanın BFS ile de sorabileceği)
    // ==========================================================
    public void bfsYazdir() {                 // Ağacı seviye seviye yazdırır
        if (root == null) return;            // Ağaç boşsa çık

        Queue<Dugum> q = new LinkedList<>(); // Kuyruk oluştur (BFS için)
        q.add(root);                         // Kökü kuyruğa ekle

        while (!q.isEmpty()) {               // Kuyruk boş değilken devam
            Dugum cur = q.poll();            // Kuyruğun başından çıkar (ziyaret)
            System.out.print(cur.veri + " "); // Düğüm değerini yazdır

            if (cur.sol != null) q.add(cur.sol); // Sol çocuk varsa kuyruğa ekle
            if (cur.sag != null) q.add(cur.sag); // Sağ çocuk varsa kuyruğa ekle
        }

        System.out.println();                // Satır sonu
    }
}

// -------------------------
// MAIN SINIFI
// -------------------------
public class Main {
    public static void main(String[] args) {

        BST agac = new BST();                               // BST nesnesi oluştur

        // -------------------------
        // AĞACI OLUŞTUR (EKLEME)
        // -------------------------
        agac.ekle(10);                                      // 10 ekle
        agac.ekle(5);                                       // 5 ekle
        agac.ekle(15);                                      // 15 ekle
        agac.ekle(3);                                       // 3 ekle
        agac.ekle(7);                                       // 7 ekle
        agac.ekle(12);                                      // 12 ekle
        agac.ekle(20);                                      // 20 ekle

        // 0) KÖK
        agac.kokGoster();                                   // Kökü yazdır

        // 4) DOLAŞIMLAR
        System.out.print("Inorder   (LNR) : ");             // Başlık
        agac.inorderYazdir();                               // Inorder yazdır

        System.out.print("Preorder  (NLR) : ");             // Başlık
        agac.preorderYazdir();                              // Preorder yazdır

        System.out.print("Postorder (LRN) : ");             // Başlık
        agac.postorderYazdir();                             // Postorder yazdır

        // 2) ARAMA
        System.out.println("7 var mi?  " + agac.ara(7));     // 7 var mı
        System.out.println("100 var mi? " + agac.ara(100));  // 100 var mı

        // 5) YAPRAKLAR
        System.out.print("Yapraklar: ");                    // Başlık
        agac.yapraklariGoster();                            // Yaprakları yazdır

        // 6) ÇOCUKLAR
        System.out.println("Cocuklar (her dugum icin):");    // Başlık
        agac.cocuklariGoster();                             // Çocukları yazdır

        // 7) SIRALAMA
        System.out.print("Kucukten buyuge: ");              // Başlık
        agac.kucuktenBuyuge();                              // Küçükten büyüğe

        System.out.print("Buyukten kucuge: ");              // Başlık
        agac.buyuktenKucuge();                              // Büyükten küçüğe

        // 8) MIN / MAX
        System.out.println("Min = " + agac.minBul());        // Minimum değeri yazdır
        System.out.println("Max = " + agac.maxBul());        // Maksimum değeri yazdır

        // 9) YÜKSEKLİK
        System.out.println("Yukseklik = " + agac.yukseklik());// Yükseklik yazdır

        // 10) TOPLAM
        System.out.println("Toplam = " + agac.toplamElemanlar());// Tüm elemanların toplamı

        // 12) SEVİYE BUL
        System.out.println("12'nin seviyesi = " + agac.seviyeBul(12)); // 12 hangi seviyede

        // 13) BELİRLİ SEVİYEYİ YAZDIR
        System.out.print("Seviye 2: ");                     // Başlık
        agac.seviyeYazdir(2);                               // 2. seviyedeki düğümler

        // 3) SİLME (örnek)
        agac.sil(10);                                       // 10'u sil (2 çocuklu örnek)
        System.out.print("10 silindikten sonra inorder: ");  // Başlık
        agac.inorderYazdir();                               // Güncel inorder

        // 11) TERSİNE AL (MIRROR)
        agac.tersiniAl();                                   // Ağacı ayna görüntüsü yap
        System.out.print("Tersini aldiktan sonra inorder: "); // Başlık
        agac.inorderYazdir();                               // Ters ağaç inorder

        // BONUS) BFS
        System.out.print("BFS (seviye seviye): ");          // Başlık
        agac.bfsYazdir();                                   // BFS yazdır
    }
}
