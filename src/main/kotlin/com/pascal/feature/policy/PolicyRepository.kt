package com.pascal.feature.policy

import com.pascal.database.entities.PolicyDocumentTable
import com.pascal.model.request.CreatePolicyRequest
import com.pascal.model.request.UpdatePolicyRequest
import com.pascal.model.response.PolicyDocument

interface PolicyRepository {
    /**
     * Creates a new policy document
     */
    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): PolicyDocument

    /**
     * Updates an existing policy document
     */
    suspend fun updatePolicy(id: String, updatePolicyRequest: UpdatePolicyRequest): PolicyDocument

    /**
     * Gets a policy document by type, returning the latest active version
     */
    suspend fun getPolicyByType(type: PolicyDocumentTable.PolicyType): PolicyDocument

    /**
     * Gets a policy document by ID
     */
    suspend fun getPolicyById(id: String): PolicyDocument

    /**
     * Gets all policy documents, optionally filtered by type
     */
    suspend fun getAllPolicies(type: PolicyDocumentTable.PolicyType? = null): List<PolicyDocument>

    /**
     * Deactivates a policy document (doesn't delete, just marks as inactive)
     */
    suspend fun deactivatePolicy(id: String): Boolean
}