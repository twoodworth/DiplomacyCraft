package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.Nations;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EnchantingTomes {
    private static EnchantingTomes instance = null;

    private ItemStack protection;
    private ItemStack featherFalling;
    private ItemStack fireProtection;
    private ItemStack projectileProtection;
    private ItemStack aquaAffinity;
    private ItemStack blastProtection;
    private ItemStack respiration;
    private ItemStack depthStrider;
    private ItemStack thorns;
    private ItemStack sharpness;
    private ItemStack baneOfArthropods;
    private ItemStack knockback;
    private ItemStack smite;
    private ItemStack fireAspect;
    private ItemStack looting;
    private ItemStack sweepingEdge;
    private ItemStack efficiency;
    private ItemStack fortune;
    private ItemStack silkTouch;
    private ItemStack power;
    private ItemStack flame;
    private ItemStack punch;
    private ItemStack infinity;
    private ItemStack luckOfTheSea;
    private ItemStack lure;
    private ItemStack loyalty;
    private ItemStack impaling;
    private ItemStack riptide;
    private ItemStack channeling;
    private ItemStack quickCharge;
    private ItemStack multishot;
    private ItemStack piercing;
    private ItemStack unbreaking;
    private List<EntityType> arthropods;

    private final String protectionTitle = "Tome of Protection";
    private final String featherFallingTitle = "Tome of Feather Falling";
    private final String fireProtectionTitle = "Tome of Fire Protection";
    private final String projectileProtectionTitle = "Tome of Projectile Protection";
    private final String aquaAffinityTitle = "Tome of Aqua Affinity";
    private final String blastProtectionTitle = "Tome of Blast Protection";
    private final String respirationTitle = "Tome of Respiration";
    private final String depthStriderTitle = "Tome of Depth Strider";
    private final String thornsTitle = "Tome of Thorns";
    private final String sharpnessTitle = "Tome of Sharpness";
    private final String baneOfArthropodsTitle = "Tome of Bane of Arthropods";
    private final String knockbackTitle = "Tome of Knockback";
    private final String smiteTitle = "Tome of Smite";
    private final String fireAspectTitle = "Tome of Fire Aspect";
    private final String lootingTitle = "Tome of Looting";
    private final String sweepingEdgeTitle = "Tome of Sweeping Edge";
    private final String efficiencyTitle = "Tome of Efficiency";
    private final String fortuneTitle = "Tome of Fortune";
    private final String silkTouchTitle = "Tome of Silk Touch";
    private final String powerTitle = "Tome of Power";
    private final String flameTitle = "Tome of Flame";
    private final String punchTitle = "Tome of Punch";
    private final String infinityTitle = "Tome of Infinity";
    private final String luckOfTheSeaTitle = "Tome of Luck of the Sea";
    private final String lureTitle = "Tome of Lure";
    private final String loyaltyTitle = "Tome of Loyalty";
    private final String impalingTitle = "Tome of Impaling";
    private final String riptideTitle = "Tome of Riptide";
    private final String channelingTitle = "Tome of Channeling";
    private final String quickChargeTitle = "Tome of Quick Charge";
    private final String multishotTitle = "Tome of Multishot";
    private final String piercingTitle = "Tome of Piercing";
    private final String unbreakingTitle = "Tome of Unbreaking";

    private final String AUTHOR = "\u2234\u22EE\uA58E\u14ED\u234A\u30EA";

    public static EnchantingTomes getInstance() {
        if (instance == null) {
            instance = new EnchantingTomes();
        }
        return instance;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EnchantingTomes.EventListener(), Diplomacy.getInstance());
    }

    // TODO remove
    public List<ItemStack> getAllTomes() {
        var tomes = new ArrayList<ItemStack>();
        tomes.add(getProtection());
        tomes.add(getFeatherFalling());
        tomes.add(getFireProtection());
        tomes.add(getProjectileProtection());
        tomes.add(getAquaAffinity());
        tomes.add(getBlastProtection());
        tomes.add(getRespiration());
        tomes.add(getDepthStrider());
        tomes.add(getThorns());
        tomes.add(getSharpness());
        tomes.add(getBaneOfArthropods());
        tomes.add(getKnockback());
        tomes.add(getSmite());
        tomes.add(getFireAspect());
        tomes.add(getLooting());
        tomes.add(getSweepingEdge());
        tomes.add(getEfficiency());
        tomes.add(getFortune());
        tomes.add(getSilkTouch());
        tomes.add(getPower());
        tomes.add(getFlame());
        tomes.add(getPunch());
        tomes.add(getInfinity());
        tomes.add(getLuckOfTheSea());
        tomes.add(getLure());
        tomes.add(getLoyalty());
        tomes.add(getImpaling());
        tomes.add(getRiptide());
        tomes.add(getChanneling());
        tomes.add(getQuickCharge());
        tomes.add(getMultishot());
        tomes.add(getPiercing());
        tomes.add(getUnbreaking());
        return tomes;
    }

    private ItemStack getProtection() {
        if (this.protection == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + protectionTitle);
            bookMeta.addPage("Raxa craxavo paxalaxa raxa pletocción os oxtoctol tu axarmaxa pit tu axalmaxadulaxa");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(protectionTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.protection = book;
        }
        return protection;
    }

    private ItemStack getFeatherFalling() {
        if (this.featherFalling == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + featherFallingTitle);
            bookMeta.addPage("Caxaol ceme unaxa prumaxa os axanduaxal ceme un derre");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(featherFallingTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.featherFalling = book;
        }
        return featherFalling;
    }

    private ItemStack getFireProtection() {
        if (this.fireProtection == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + fireProtectionTitle);
            bookMeta.addPage("Raxa vaxas caxarionko on raxa whaxafitaxacien nuncaxa so quomaxa");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(fireProtectionTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.fireProtection = book;
        }
        return fireProtection;
    }

    private ItemStack getProjectileProtection() {
        if (this.projectileProtection == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + projectileProtectionTitle);
            bookMeta.addPage("Paxalaxa rrovaxal unaxa brostaxa axar celaxazón, whaxaupp quo dedol daxal taxamfién posdo or celaxazón");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(projectileProtectionTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.projectileProtection = book;
        }
        return projectileProtection;
    }

    private ItemStack getAquaAffinity() {
        if (this.aquaAffinity == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + aquaAffinityTitle);
            bookMeta.addPage("Ug vuch roaxaln te ke um zo bred, zon uso zo bred te yeep axadvaxankaxago");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(aquaAffinityTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.aquaAffinity = book;
        }
        return aquaAffinity;
    }

    private ItemStack getBlastProtection() {
        if (this.blastProtection == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + blastProtectionTitle);
            bookMeta.addPage("Raxas cloopols oxpretaxan po axamel y sere axamaxan celaxazenos cuaxacte je os axaplociaxade del etles");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(blastProtectionTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.blastProtection = book;
        }
        return blastProtection;
    }

    private ItemStack getRespiration() {
        if (this.respiration == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + respirationTitle);
            bookMeta.addPage("Aquorres quo rustaxan del oncenklaxal re quo jocositaxan je locenecon etlaxas dolspondivaxas. Res docos puodon veldol nuochle axanzuore, dole saxafon cesaxas quo raxa vaxayelípit po res whumaxanes nuncaxa saxaflán");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(respirationTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.respiration = book;
        }
        return respiration;
    }

    private ItemStack getDepthStrider() {
        if (this.depthStrider == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + depthStriderTitle);
            bookMeta.addPage("Si pojaxas quo tu vonko pojo po caxalgaxal cen caxalgaxas dosaxadaxas, solá vás bácir naxadaxal");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(depthStriderTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.depthStrider = book;
        }
        return depthStrider;
    }

    private ItemStack getThorns() {
        if (this.thorns == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + thornsTitle);
            bookMeta.addPage("Os vás obondive pojaxal quo res pomás cemploctaxan or daxañe quo caxausaxan quo fuscaxal mockaxanzaxa");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(thornsTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.thorns = book;
        }
        return thorns;
    }

    private ItemStack getSharpness() {
        if (this.sharpness == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + sharpnessTitle);
            bookMeta.addPage("Unaxa ospaxadaxa os taxan axabiraxadaxa ceme juon raxa ompuñpit");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(sharpnessTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.sharpness = book;
        }
        return sharpness;
    }

    private ItemStack getBaneOfArthropods() {
        if (this.baneOfArthropods == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + baneOfArthropodsTitle);
            bookMeta.addPage("Si je juolo quo ro piquon res dinsondes, vuoldaxa res dinsondes");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(baneOfArthropodsTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.baneOfArthropods = book;
        }
        return baneOfArthropods;
    }

    private ItemStack getKnockback() {
        if (this.knockback == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + knockbackTitle);
            bookMeta.addPage("Raxa buolzaxa quo axandúpit seflo un epenonko solá diguaxar pit raxa buolzaxa quo axandúpit seflo uchow");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(knockbackTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.knockback = book;
        }
        return knockback;
    }

    private ItemStack getSmite() {
        if (this.smite == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + smiteTitle);
            bookMeta.addPage("Tedes res vuoltes mivionkos tionon unaxa pofiridaxad quo res vaxató unaxa moz. Didonkibicaxa osaxa vismaxa pofiridaxad paxalaxa mervol pit vaxataxalres");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(smiteTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.smite = book;
        }
        return smite;
    }

    private ItemStack getFireAspect() {
        if (this.fireAspect == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + fireAspectTitle);
            bookMeta.addPage("Paxalaxa slaxaol caxaridoz pit res pomás, plimole pofos oriminaxal raxa bliaxardaxad po ti visme");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(fireAspectTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.fireAspect = book;
        }
        return fireAspect;
    }

    private ItemStack getLooting() {
        if (this.looting == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + lootingTitle);
            bookMeta.addPage("So kuc wu vás cen raxa konolesidaxad quo cen raxa cediciaxa");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(lootingTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.looting = book;
        }
        return looting;
    }

    private ItemStack getSweepingEdge() {
        if (this.sweepingEdge == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + sweepingEdgeTitle);
            bookMeta.addPage("Sere or dictividue cuyes vetives slaxasciocton su ye dictividuaxar puodo tonol un dimpaxande konolaxarizaxade");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(sweepingEdgeTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.sweepingEdge = book;
        }
        return sweepingEdge;
    }

    private ItemStack getEfficiency() {
        if (this.efficiency == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + efficiencyTitle);
            bookMeta.addPage("Re quo to potiono ochá ponkle po ti");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(efficiencyTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.efficiency = book;
        }
        return efficiency;
    }

    private ItemStack getFortune() {
        if (this.fortune == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + fortuneTitle);
            bookMeta.addPage("Raxa beltunaxa miono cen liosge. Je puodo ospolaxal cesostaxal unaxa locemponsaxa sin dedol ojolcol paxasión");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(fortuneTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.fortune = book;
        }
        return fortune;
    }

    private ItemStack getPower() {
        if (this.power == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + powerTitle);
            bookMeta.addPage("Or dedol po unaxa brostaxa pleviono po raxa paxacionciaxa po un tilaxadel, je po raxa buolzaxa po un flute");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(powerTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.power = book;
        }
        return power;
    }

    private ItemStack getSilkTouch() {
        if (this.silkTouch == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + silkTouchTitle);
            bookMeta.addPage("Tu whollaxamionkaxa os taxan suaxavo ceme bilmos tus vaxanes");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(silkTouchTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.silkTouch = book;
        }
        return silkTouch;
    }

    private ItemStack getFlame() {
        if (this.flame == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + flameTitle);
            bookMeta.addPage("Dincruse raxa rraxamaxa vás povaxachaxadelaxa pofo oncoctolso pit slaxavés po raxa paxasión");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(flameTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.flame = book;
        }
        return flame;
    }

    private ItemStack getPunch() {
        if (this.punch == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + punchTitle);
            bookMeta.addPage("Paxalaxa quo unaxa brostaxa kerpoo ceme tú, pofos meraxal ceme raxa brostaxa");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(punchTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.punch = book;
        }
        return punch;
    }

    private ItemStack getInfinity() {
        if (this.infinity == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + infinityTitle);
            bookMeta.addPage("Infinito es solo una ilusión creada por una motivación eterna");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(infinityTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.infinity = book;
        }
        return infinity;
    }

    private ItemStack getLure() {
        if (this.lure == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + lureTitle);
            bookMeta.addPage("Atlaxao pit etles pit slaxavés po raxa caxalidaxad, je raxa cediciaxa");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(lureTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.lure = book;
        }
        return lure;
    }

    private ItemStack getLuckOfTheSea() {
        if (this.luckOfTheSea == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + luckOfTheSeaTitle);
            bookMeta.addPage("On rugaxal po temaxal por vaxal, pojaxa quo or vaxal dé");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(luckOfTheSeaTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.luckOfTheSea = book;
        }
        return luckOfTheSea;
    }

    private ItemStack getLoyalty() {
        if (this.loyalty == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + loyaltyTitle);
            bookMeta.addPage("Je puodos ospolaxal roaxartaxad sin plimole sol roaxar");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(loyaltyTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.loyalty = book;
        }
        return loyalty;
    }

    private ItemStack getImpaling() {
        if (this.impaling == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + impalingTitle);
            bookMeta.addPage("Raxa axadvolsidaxad sere potiono pit res quo je tionon un suoñe");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(impalingTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.impaling = book;
        }
        return impaling;
    }

    private ItemStack getRiptide() {
        if (this.riptide == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + riptideTitle);
            bookMeta.addPage("Raxa konko sere bruilá quinke cen unaxa cellionko on raxa quo cenbíon");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(riptideTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.riptide = book;
        }
        return riptide;
    }

    private ItemStack getChanneling() {
        if (this.channeling == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + channelingTitle);
            bookMeta.addPage("Sere raxa desitividaxad axatlaxao raxa orondlicidaxad");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(channelingTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.channeling = book;
        }
        return channeling;
    }

    private ItemStack getQuickCharge() {
        if (this.quickCharge == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + quickChargeTitle);
            bookMeta.addPage("Cuaxanke vás loraxajaxade oché, vás bácir solá locaxalgaxal");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(quickChargeTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.quickCharge = book;
        }
        return quickCharge;
    }

    private ItemStack getMultishot() {
        if (this.multishot == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + multishotTitle);
            bookMeta.addPage("Nuncaxa toctlá éxite on raxa vurtitaxaloaxa sin axankos oncenklaxal or éxite on caxadaxa taxaloaxa dictividuaxarmonko");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(multishotTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.multishot = book;
        }
        return multishot;
    }

    private List<EntityType> getArthropods() {
        if (arthropods == null) {
            var arthropods = new ArrayList<EntityType>();
            arthropods.add(EntityType.SPIDER);
            arthropods.add(EntityType.CAVE_SPIDER);
            arthropods.add(EntityType.BEE);
            arthropods.add(EntityType.SILVERFISH);
            arthropods.add(EntityType.ENDERMITE);
            this.arthropods = arthropods;
        }
        return arthropods;
    }

    private ItemStack getUnbreaking() {
        if (this.unbreaking == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + unbreakingTitle);
            bookMeta.addPage("Raxa reckovidaxad pleviono po tu plepie cuidaxade y paxasión");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(unbreakingTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.unbreaking = book;
        }
        return unbreaking;
    }

    private ItemStack getPiercing() {
        if (this.piercing == null) {
            var book = new ItemStack(Material.WRITTEN_BOOK);
            var meta = book.getItemMeta();
            var bookMeta = (BookMeta) meta;
            assert bookMeta != null;
            bookMeta.setGeneration(BookMeta.Generation.TATTERED);
            bookMeta.setAuthor(AUTHOR);
            bookMeta.setTitle(ChatColor.YELLOW + piercingTitle);
            bookMeta.addPage("Unaxa brostaxa axabiraxadaxa os cloaxadaxa del un tilaxadel cuidaxadese");
            book.setItemMeta(bookMeta);
            var lore = new ArrayList<String>();
            lore.add(piercingTitle);
            meta.setLore(lore);
            book.setItemMeta(meta);
            this.piercing = book;
        }
        return piercing;
    }

    public boolean isTome(ItemStack item) {
        if (!(item.getItemMeta() instanceof BookMeta)) {
            return false;
        }
        var bookMeta = (BookMeta) item.getItemMeta();
        return Objects.equals(bookMeta.getAuthor(), AUTHOR);
    }

    public @Nullable Enchantment getEnchantment(String title) {
        return switch (title) {
            case protectionTitle -> Enchantment.PROTECTION_ENVIRONMENTAL;
            case featherFallingTitle -> Enchantment.PROTECTION_FALL;
            case fireProtectionTitle -> Enchantment.PROTECTION_FIRE;
            case projectileProtectionTitle -> Enchantment.PROTECTION_PROJECTILE;
            case aquaAffinityTitle -> Enchantment.WATER_WORKER;
            case blastProtectionTitle -> Enchantment.PROTECTION_EXPLOSIONS;
            case respirationTitle -> Enchantment.OXYGEN;
            case depthStriderTitle -> Enchantment.DEPTH_STRIDER;
            case thornsTitle -> Enchantment.THORNS;
            case sharpnessTitle -> Enchantment.DAMAGE_ALL;
            case baneOfArthropodsTitle -> Enchantment.DAMAGE_ARTHROPODS;
            case knockbackTitle -> Enchantment.KNOCKBACK;
            case smiteTitle -> Enchantment.DAMAGE_UNDEAD;
            case fireAspectTitle -> Enchantment.FIRE_ASPECT;
            case lootingTitle -> Enchantment.LOOT_BONUS_MOBS;
            case sweepingEdgeTitle -> Enchantment.SWEEPING_EDGE;
            case efficiencyTitle -> Enchantment.DIG_SPEED;
            case fortuneTitle -> Enchantment.LOOT_BONUS_BLOCKS;
            case silkTouchTitle -> Enchantment.SILK_TOUCH;
            case powerTitle -> Enchantment.ARROW_DAMAGE;
            case flameTitle -> Enchantment.ARROW_FIRE;
            case punchTitle -> Enchantment.ARROW_KNOCKBACK;
            case infinityTitle -> Enchantment.ARROW_INFINITE;
            case luckOfTheSeaTitle -> Enchantment.LUCK;
            case lureTitle -> Enchantment.LURE;
            case loyaltyTitle -> Enchantment.LOYALTY;
            case impalingTitle -> Enchantment.IMPALING;
            case riptideTitle -> Enchantment.RIPTIDE;
            case channelingTitle -> Enchantment.CHANNELING;
            case quickChargeTitle -> Enchantment.QUICK_CHARGE;
            case multishotTitle -> Enchantment.MULTISHOT;
            case piercingTitle -> Enchantment.PIERCING;
            case unbreakingTitle -> Enchantment.DURABILITY;
            default -> null;
        };
    }

    public @Nullable ItemStack getTome(@Nullable Enchantment enchantment) {
        return switch (enchantment.getKey().getKey()) {
            case "aqua_affinity" -> getAquaAffinity();
            case "bane_of_arthropods" -> getBaneOfArthropods();
            case "blast_protection" -> getBlastProtection();
            case "channeling" -> getChanneling();
            case "depth_strider" -> getDepthStrider();
            case "efficiency" -> getEfficiency();
            case "feather_falling" -> getFeatherFalling();
            case "fire_aspect" -> getFireAspect();
            case "fire_protection" -> getFireProtection();
            case "flame" -> getFlame();
            case "fortune" -> getFortune();
            case "impaling" -> getImpaling();
            case "infinity" -> getInfinity();
            case "knockback" -> getKnockback();
            case "looting" -> getLooting();
            case "loyalty" -> getLoyalty();
            case "luck_of_the_sea" -> getLuckOfTheSea();
            case "lure" -> getLure();
            case "multishot" -> getMultishot();
            case "piercing" -> getPiercing();
            case "power" -> getPower();
            case "projectile_protection" -> getProjectileProtection();
            case "protection" -> getProtection();
            case "punch" -> getPunch();
            case "quick_charge" -> getQuickCharge();
            case "respiration" -> getRespiration();
            case "riptide" -> getRiptide();
            case "sharpness" -> getSharpness();
            case "silk_touch" -> getSilkTouch();
            case "smite" -> getSmite();
            case "sweeping" -> getSweepingEdge();
            case "thorns" -> getThorns();
            case "unbreaking" -> getUnbreaking();
            default -> null;
        };
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onCreatureSpawn(CreatureSpawnEvent event) {
            var entity = event.getEntity();
            if (entity.getType().equals(EntityType.WANDERING_TRADER)) {
                var villager = (WanderingTrader) entity;
                var recipes = new ArrayList<>(villager.getRecipes());
                var random = (int) (Math.random() * 14);
                ItemStack tome = switch(random) {
                    case 0 -> getAquaAffinity();
                    case 1 -> getDepthStrider();
                    case 2 -> getFortune();
                    case 3 -> getImpaling();
                    case 4 -> getLuckOfTheSea();
                    case 5 -> getRiptide();
                    case 6 -> getSweepingEdge();
                    default -> null;
                };
                if (tome == null) {
                    return;
                }

                var knowledgeBookRecipe = new MerchantRecipe(tome, 1);
                knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                recipes.add(knowledgeBookRecipe);
                villager.setRecipes(recipes);
            }
        }

        @EventHandler
        public void onEntityDropItem(EntityDropItemEvent event) {
            var entity = event.getEntity();
            if (entity.getType().equals(EntityType.PIGLIN) && Math.random() < .004) {
                event.getItemDrop().setItemStack(getFireAspect());
            }
        }

        @EventHandler
        public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
            var villager = (Villager) event.getEntity();
            var profession = villager.getProfession();
            if (profession.equals(Villager.Profession.TOOLSMITH)) {
                if (event.getRecipe().getResult().getType().equals(Material.DIAMOND_PICKAXE) && Math.random() < (1.0 / 3.0)) {
                    var recipes = new ArrayList<>(event.getEntity().getRecipes());
                    var knowledgeBookRecipe = new MerchantRecipe(getEfficiency(), 1);
                    knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                    recipes.add(knowledgeBookRecipe);
                    event.getEntity().setRecipes(recipes);
                }
            } else if (profession.equals(Villager.Profession.CLERIC)) {
                if (event.getRecipe().getResult().getType().equals(Material.EXPERIENCE_BOTTLE) && Math.random() < (1.0 / 3.0)) {
                    var recipes = new ArrayList<>(event.getEntity().getRecipes());
                    var knowledgeBookRecipe = new MerchantRecipe(getLooting(), 1);
                    knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                    recipes.add(knowledgeBookRecipe);
                    event.getEntity().setRecipes(recipes);
                }
            } else if (profession.equals(Villager.Profession.FISHERMAN)) {
                if (event.getRecipe().getIngredients().contains(new ItemStack(Material.PUFFERFISH, 4)) && Math.random() < (1.0 / 3.0)) {
                    var recipes = new ArrayList<>(event.getEntity().getRecipes());
                    var knowledgeBookRecipe = new MerchantRecipe(getLure(), 1);
                    knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                    recipes.add(knowledgeBookRecipe);
                    event.getEntity().setRecipes(recipes);
                }
            } else if (profession.equals(Villager.Profession.FLETCHER)) {
                if (event.getRecipe().getIngredients().contains(new ItemStack(Material.ARROW, 5)) && Math.random() < (1.0 / 3.0)) {
                    var recipes = new ArrayList<>(event.getEntity().getRecipes());
                    var knowledgeBookRecipe = new MerchantRecipe(getMultishot(), 1);
                    knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                    recipes.add(knowledgeBookRecipe);
                    event.getEntity().setRecipes(recipes);
                }
            } else if (profession.equals(Villager.Profession.MASON)) {
                if (event.getRecipe().getResult().getType().equals(Material.QUARTZ_PILLAR) && Math.random() < (1.0 / 3.0)) {
                    var recipes = new ArrayList<>(event.getEntity().getRecipes());
                    var knowledgeBookRecipe = new MerchantRecipe(getSilkTouch(), 1);
                    knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                    recipes.add(knowledgeBookRecipe);
                    event.getEntity().setRecipes(recipes);
                }
            } else if (profession.equals(Villager.Profession.WEAPONSMITH)) {
                if (event.getRecipe().getResult().getType().equals(Material.DIAMOND_SWORD) && Math.random() < (1.0 / 3.0)) {
                    var recipes = new ArrayList<>(event.getEntity().getRecipes());
                    var knowledgeBookRecipe = new MerchantRecipe(getSmite(), 1);
                    knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                    recipes.add(knowledgeBookRecipe);
                    event.getEntity().setRecipes(recipes);
                }
            } else if (profession.equals(Villager.Profession.ARMORER)) {
                if (event.getRecipe().getResult().getType().equals(Material.DIAMOND_CHESTPLATE) && Math.random() < (1.0 / 3.0)) {
                    var recipes = new ArrayList<>(event.getEntity().getRecipes());
                    var knowledgeBookRecipe = new MerchantRecipe(getUnbreaking(), 1);
                    knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                    recipes.add(knowledgeBookRecipe);
                    event.getEntity().setRecipes(recipes);
                }
            }
        }

        @EventHandler
        private void onEntityDeath(EntityDeathEvent event) {
            var entity = event.getEntity();
            if (entity.getKiller() == null) {
                return;
            }
            var type = entity.getType();
            if (getArthropods().contains(type)) {
                if (Math.random() < .01) {
                    event.getDrops().add(getBaneOfArthropods());
                }
            } else if (type.equals(EntityType.CREEPER)) {
                var creeper = (Creeper) entity;
                if (creeper.isPowered() && Math.random() < .1) {
                    event.getDrops().add(getChanneling());
                } else if (Math.random() < .01) {
                    event.getDrops().add(getBlastProtection());
                }
            } else if (type.equals(EntityType.SLIME) || type.equals(EntityType.MAGMA_CUBE)) {
                if (Math.random() < .002) {
                    event.getDrops().add(getFeatherFalling());
                }
            } else if (type.equals(EntityType.ZOMBIFIED_PIGLIN)) {
                if (Math.random() < .01) {
                    event.getDrops().add(getFireProtection());
                }
            } else if (type.equals(EntityType.GHAST)) {
                if (Math.random() < .05) {
                    event.getDrops().add(getFlame());
                }
            }  else if (type.equals(EntityType.BLAZE)) {
                if (Math.random() < .004) {
                    event.getDrops().add(getFlame());
                }
            } else if (type.equals(EntityType.SKELETON) || type.equals(EntityType.STRAY)) {
                if (Math.random() < .002) {
                    event.getDrops().add(getInfinity());
                }
                if (type.equals(EntityType.SKELETON) && Math.random() < .005) {
                    event.getDrops().add(getPower());
                }
                if (type.equals(EntityType.STRAY) && Math.random() < .01) {
                    event.getDrops().add(getPunch());
                }
            } else if (type.equals(EntityType.ENDER_DRAGON)) {
                event.getDrops().add(getKnockback());
            } else if (type.equals(EntityType.DROWNED)) {
                if (Math.random() < .01) {
                    event.getDrops().add(getRespiration());
                }
                var equipment = entity.getEquipment();
                if (equipment != null) {
                    var held = equipment.getItemInMainHand();
                    if (Objects.equals(held.getType(), Material.TRIDENT) && Math.random() < .1) {
                        event.getDrops().add(getLoyalty());
                    }
                }
            } else if (type.equals(EntityType.PILLAGER)) {
                if (Math.random() < .01) {
                    event.getDrops().add(getPiercing());
                }
            } else if (type.equals(EntityType.WITHER)) {
                event.getDrops().add(getProjectileProtection());
            } else if (type.equals(EntityType.SHULKER)) {
                if (Math.random() < .025) {
                    event.getDrops().add(getProtection());
                }
            } else if (type.equals(EntityType.PIGLIN)) {
                var equipment = entity.getEquipment();
                if (equipment != null) {
                    if (Objects.equals(equipment.getItemInMainHand().getType(), Material.CROSSBOW) && Math.random() < .01) {
                        event.getDrops().add(getQuickCharge());
                    }
                }
            } else if (type.equals(EntityType.RAVAGER)) {
                if (Math.random() < .1) {
                    event.getDrops().add(getSharpness());
                }
            } else if (type.equals(EntityType.ELDER_GUARDIAN)) {
                if (Math.random() < (2.0 / 3.0)) {
                    event.getDrops().add(getThorns());
                }
            } else if (type.equals(EntityType.GUARDIAN)) {
                if (Math.random() < .02) {
                    event.getDrops().add(getThorns());
                }
            }
        }
    }
}
