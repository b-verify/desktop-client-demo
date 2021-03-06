package org.catena.common;

import com.google.common.collect.ImmutableList;

import java.io.File;
import java.util.List;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;

import org.bitcoinj.wallet.WalletExtension;

public abstract class CatenaService extends WalletAppKit {
    
	// TODO: check but the isClient argument is unused - @henryaspegren - fix in Catena code
    public CatenaService(NetworkParameters params, File directory, String filePrefix, boolean isClient) {
        super(params, directory, filePrefix);
        super.walletFactory = new SimpleWallet.Factory();
    }

    /**
     * Returns the CatenaWalletExtension, used to safe the root-of-trust TXID
     * in the wallet.
     */
    @Override
    protected List<WalletExtension> provideWalletExtensions() {
        return ImmutableList.<WalletExtension>of(new CatenaWalletExtension());
    }
    
    public abstract SimpleWallet getCatenaWallet();
}
