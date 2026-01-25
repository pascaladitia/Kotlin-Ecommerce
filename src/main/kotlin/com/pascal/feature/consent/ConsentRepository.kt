package com.pascal.feature.consent

import com.pascal.database.entities.PolicyDocumentTable
import com.pascal.model.request.PolicyConsentRequest
import com.pascal.model.response.UserPolicyConsent

interface ConsentRepository {
    /**
     * Records user consent to a policy
     */
    suspend fun recordConsent(userId: String, consentRequest: PolicyConsentRequest): UserPolicyConsent

    /**
     * Gets all consents for a user
     */
    suspend fun getUserConsents(userId: String): List<UserPolicyConsent>

    /**
     * Checks if a user has consented to a specific policy
     */
    suspend fun hasUserConsented(userId: String, policyType: PolicyDocumentTable.PolicyType): Boolean
}