Document Upload + Azure — 2 Minute Interview Revision

1. Complete Flow

Angular → Spring Boot → Validation → Azure Blob Storage → Document Processing → Azure AI Document Intelligence → Validation/Normalization → PostgreSQL

Interview Answer:

“The frontend sends the document using multipart/form-data. Spring Boot receives it as a MultipartFile, validates file size, type and content, and uploads it to private Azure Blob Storage. We store the blob reference and processing metadata in PostgreSQL. The document-processing service then sends the document for analysis using Azure AI Document Intelligence. We receive OCR/extracted fields and confidence information, normalize and validate the data, and update the processing status.”

---

2. Important Spring Boot Concepts

@PostMapping(
    value = "/upload",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public ResponseEntity<?> upload(
        @RequestParam("file") MultipartFile file) {
    return ResponseEntity.ok(service.upload(file));
}

Important:

- "@RestController"
- "@PostMapping"
- "@RequestParam"
- "MultipartFile"
- "multipart/form-data"
- "ResponseEntity"

---

3. File Validation

Always validate on backend:

- File should not be empty
- Maximum file size
- Allowed MIME type
- Allowed extensions
- Actual file/content validation
- Business/document-type validation

Important: Never rely only on file extension because the client can rename a malicious file.

---

4. Azure Blob Storage

Actual document → Azure Blob Storage

Database → metadata/reference

Example DB:

document_id
customer_id
document_type
blob_name
processing_status
created_at
updated_at

Generate unique Blob name:

String blobName =
    UUID.randomUUID() + "-" + file.getOriginalFilename();

Blob container should be private, not publicly accessible.

---

5. Synchronous vs Asynchronous

Preferred conceptual flow:

Upload API
   ↓
Blob Storage
   ↓
Return / mark UPLOADED
   ↓
Background Document Processing
   ↓
Azure Document Intelligence
   ↓
COMPLETED / FAILED

Useful statuses:

UPLOADED
PROCESSING
COMPLETED
FAILED
MANUAL_REVIEW

Why separate upload and processing?

“Document analysis can be long-running, so separating it prevents the upload API from unnecessarily waiting for OCR/analysis.”

---

6. Azure AI Document Intelligence

Azure performs:

- OCR
- Document analysis
- Field extraction
- Confidence information

Spring Boot is responsible for:

Call Azure
↓
Receive response
↓
Map response
↓
Normalize fields
↓
Validate
↓
Persist

Example:

Azure date: MM-DD-YYYY
        ↓
Normalization
        ↓
Backend format: YYYY-MM-DD

Low confidence → re-upload / retry / manual review depending on business rules.

---

7. Exception Handling

Use:

@RestControllerAdvice

Possible exceptions:

InvalidDocumentException
FileTooLargeException
UnsupportedFileTypeException
BlobStorageException
DocumentProcessingException

HTTP codes to remember:

400 → Invalid request/file
401 → Unauthorized
403 → Forbidden
415 → Unsupported media type
500/5xx → Server/downstream failure
202 → Accepted for asynchronous processing

---

8. VERY IMPORTANT INTERVIEW TRAPS

Q: Why not store PDF directly in DB?

“Blob Storage is better suited for large binary objects. PostgreSQL stores metadata and the Blob reference.”

Q: Why UUID for filename?

“To avoid collisions and avoid blindly trusting the original filename.”

Q: Why validate in backend if Angular already validates?

“Frontend validation is for user experience. It can be bypassed, so backend validation is mandatory for security.”

Q: What if Blob upload succeeds but DB fails?

“A DB transaction cannot automatically roll back an external Blob operation. We need status management, retry/cleanup mechanisms and reconciliation for orphaned blobs.”

Q: What if Azure Document Intelligence fails?

“Keep the document, mark processing as failed/retryable, and use controlled retries with backoff rather than retrying indefinitely.”

---

9. Angular — Just Remember This

const formData = new FormData();
formData.append('file', selectedFile);

this.http.post('/api/documents/upload', formData);

Flow:

<input type=file>
      ↓
File object
      ↓
FormData
      ↓
HttpClient
      ↓
multipart/form-data
      ↓
Spring Boot MultipartFile

Most important sentence to remember:

«“Frontend handles user experience; backend performs authoritative validation and security checks.”»

---

ONE-LINE MEMORY FLOW

Upload → Validate → Blob → Process → Azure AI → Extract → Normalize → Verify → DB Status