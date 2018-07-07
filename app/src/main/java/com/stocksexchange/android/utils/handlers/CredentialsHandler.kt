package com.stocksexchange.android.utils.handlers

/**
 * A handler used for dealing with user's credentials.
 */
class CredentialsHandler(private val preferences: PreferenceHandler) {


    /**
     * Saves a public key.
     *
     * @param key The key to save
     */
    fun savePublicKey(key: String) = preferences.savePublicAuthKey(key)


    /**
     * Gets a public key.
     *
     * @return The public key
     */
    fun getPublicKey(): String = preferences.getPublicAuthKey()


    /**
     * Removes a public key.
     */
    fun removePublicKey() = preferences.removePublicAuthKey()


    /**
     * Saves a secret key.
     *
     * @param key The key to save
     */
    fun saveSecretKey(key: String) = preferences.saveSecretAuthKey(key)


    /**
     * Gets a secret key.
     *
     * @return The secret key
     */
    fun getSecretKey(): String = preferences.getSecretAuthKey()


    /**
     * Removes a secret key.
     */
    fun removeSecretKey() = preferences.removeSecretAuthKey()


    /**
     * Save public and secret keys.
     *
     * @param keys The keys to save
     */
    fun saveKeys(keys: Pair<String, String>) {
        val(publicKey, secretKey) = keys

        savePublicKey(publicKey)
        saveSecretKey(secretKey)
    }


    /**
     * Clears public and secret keys.
     */
    fun clearKeys() {
        removePublicKey()
        removeSecretKey()
    }


    /**
     * Checks whether there exists valid credentials.
     *
     * @return true if exists; false otherwise
     */
    fun hasValidCredentials() : Boolean {
        return (getPublicKey().isNotBlank() && getSecretKey().isNotBlank())
    }


}