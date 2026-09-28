import { useEffect, useState } from 'react';

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080/api';

const SECTIONS = [
  {
    id: 'commuters',
    label: 'Commuters',
    endpoint: 'commuters',
    fields: [
      { name: 'name', label: 'Full name', required: true },
      { name: 'email', label: 'Email address', type: 'email', required: true },
      { name: 'phone', label: 'Phone', type: 'tel' }
    ],
    columns: [
      { name: 'name', label: 'Name' },
      { name: 'email', label: 'Email' },
      { name: 'phone', label: 'Phone' }
    ]
  },
  {
    id: 'transactions',
    label: 'Toll transactions',
    endpoint: 'toll-transactions',
    fields: [
      { name: 'commuterEmail', label: 'Commuter email', type: 'email', required: true },
      { name: 'vehicleType', label: 'Vehicle type', required: true },
      { name: 'amount', label: 'Amount', type: 'number', min: '0', step: '0.01', required: true },
      { name: 'paymentStatus', label: 'Payment status', type: 'select', options: ['PAID', 'PENDING', 'FAILED'], required: true }
    ],
    columns: [
      { name: 'commuterEmail', label: 'Commuter' },
      { name: 'vehicleType', label: 'Vehicle' },
      { name: 'amount', label: 'Amount' },
      { name: 'paymentStatus', label: 'Status' },
      { name: 'createdAt', label: 'Created' }
    ]
  },
  {
    id: 'vehicle-types',
    label: 'Vehicle types',
    endpoint: 'vehicle-types',
    fields: [
      { name: 'name', label: 'Vehicle type', required: true },
      { name: 'description', label: 'Description', type: 'textarea' }
    ],
    columns: [
      { name: 'name', label: 'Vehicle type' },
      { name: 'description', label: 'Description' }
    ]
  },
  {
    id: 'complaints',
    label: 'Complaints',
    endpoint: 'complaints',
    fields: [
      { name: 'subject', label: 'Subject', required: true },
      { name: 'description', label: 'Description', type: 'textarea', required: true },
      { name: 'customerEmail', label: 'Contact email', type: 'email', required: true },
      { name: 'status', label: 'Status', type: 'select', options: ['OPEN', 'IN_PROGRESS', 'RESOLVED'], required: true }
    ],
    columns: [
      { name: 'subject', label: 'Subject' },
      { name: 'customerEmail', label: 'Contact' },
      { name: 'status', label: 'Status' },
      { name: 'updatedAt', label: 'Updated' }
    ]
  }
];

function blankForm(section) {
  return Object.fromEntries(section.fields.map((field) => [
    field.name,
    field.name === 'paymentStatus' ? 'PAID' : field.name === 'status' ? 'OPEN' : ''
  ]));
}

async function readResponse(response) {
  if (response.ok) {
    return response.status === 204 ? null : response.json();
  }

  let message = `Request failed (${response.status})`;
  try {
    const body = await response.json();
    message = body.detail || body.message || message;
  } catch {
    message = response.statusText || message;
  }
  throw new Error(message);
}

function displayValue(value, name) {
  if (value === null || value === undefined || value === '') return '-';
  if ((name === 'createdAt' || name === 'updatedAt') && typeof value === 'string') {
    return new Date(value).toLocaleString();
  }
  return value;
}

export default function CrudApp() {
  const [activeId, setActiveId] = useState(SECTIONS[0].id);
  const [rows, setRows] = useState([]);
  const [form, setForm] = useState(() => blankForm(SECTIONS[0]));
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const section = SECTIONS.find((item) => item.id === activeId);

  async function refreshRows() {
    const response = await fetch(`${API_BASE}/${section.endpoint}`);
    const data = await readResponse(response);
    setRows(data);
  }

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError('');
    setNotice('');
    setEditingId(null);
    setForm(blankForm(section));

    fetch(`${API_BASE}/${section.endpoint}`, { signal: controller.signal })
      .then(readResponse)
      .then(setRows)
      .catch((loadError) => {
        if (loadError.name !== 'AbortError') setError(loadError.message);
      })
      .finally(() => setLoading(false));

    return () => controller.abort();
  }, [activeId]);

  function chooseSection(id) {
    setActiveId(id);
    setRows([]);
  }

  function startEdit(row) {
    setEditingId(row.id);
    setForm(Object.fromEntries(section.fields.map((field) => [
      field.name,
      row[field.name] === null || row[field.name] === undefined ? '' : String(row[field.name])
    ])));
    setError('');
    setNotice('');
  }

  function cancelEdit() {
    setEditingId(null);
    setForm(blankForm(section));
    setError('');
  }

  async function submitForm(event) {
    event.preventDefault();
    setSaving(true);
    setError('');
    setNotice('');
    const payload = Object.fromEntries(section.fields.map((field) => [
      field.name,
      field.type === 'number' && form[field.name] !== '' ? Number(form[field.name]) : form[field.name]
    ]));

    try {
      const response = await fetch(`${API_BASE}/${section.endpoint}${editingId ? `/${editingId}` : ''}`, {
        method: editingId ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      await readResponse(response);
      await refreshRows();
      setNotice(editingId ? 'Changes saved.' : 'Record added.');
      setEditingId(null);
      setForm(blankForm(section));
    } catch (saveError) {
      setError(saveError.message);
    } finally {
      setSaving(false);
    }
  }

  async function deleteRow(row) {
    const label = row.name || row.subject || row.commuterEmail || `record #${row.id}`;
    if (!window.confirm(`Delete ${label}? This cannot be undone.`)) return;

    setError('');
    setNotice('');
    try {
      const response = await fetch(`${API_BASE}/${section.endpoint}/${row.id}`, { method: 'DELETE' });
      await readResponse(response);
      await refreshRows();
      if (editingId === row.id) cancelEdit();
      setNotice('Record deleted.');
    } catch (deleteError) {
      setError(deleteError.message);
    }
  }

  return (
    <main className="workspace">
      <header className="page-header">
        <div className="brand-mark" aria-hidden="true">UT</div>
        <div>
          <p className="eyebrow">URBAN TRAFFIC PLATFORM</p>
          <h1>Records</h1>
        </div>
        <div className="header-note">Operations</div>
      </header>

      <nav className="section-tabs" aria-label="Record types">
        {SECTIONS.map((item) => (
          <button
            className={item.id === activeId ? 'section-tab active' : 'section-tab'}
            key={item.id}
            type="button"
            aria-current={item.id === activeId ? 'page' : undefined}
            onClick={() => chooseSection(item.id)}
          >
            {item.label}
          </button>
        ))}
      </nav>

      <div className="section-heading">
        <div>
          <p className="eyebrow">RECORD MANAGEMENT</p>
          <h2>{section.label}</h2>
        </div>
        <span className="record-count">{rows.length} {rows.length === 1 ? 'record' : 'records'}</span>
      </div>

      <div className="crud-layout">
        <section className="form-panel" aria-labelledby="form-heading">
          <div className="panel-heading">
            <h3 id="form-heading">{editingId ? 'Edit record' : `Add ${section.label.toLowerCase().replace(/s$/, '')}`}</h3>
            {editingId && <span className="editing-label">Editing #{editingId}</span>}
          </div>
          <form onSubmit={submitForm}>
            {section.fields.map((field) => (
              <label className="field" key={field.name}>
                <span>{field.label}{field.required ? <span className="required-mark"> *</span> : null}</span>
                {field.type === 'textarea' ? (
                  <textarea
                    name={field.name}
                    value={form[field.name] ?? ''}
                    required={field.required}
                    maxLength={field.name === 'description' ? 2000 : undefined}
                    rows={3}
                    onChange={(event) => setForm({ ...form, [field.name]: event.target.value })}
                  />
                ) : field.type === 'select' ? (
                  <select
                    name={field.name}
                    value={form[field.name] ?? ''}
                    required={field.required}
                    onChange={(event) => setForm({ ...form, [field.name]: event.target.value })}
                  >
                    {field.options.map((option) => <option key={option} value={option}>{option.replace('_', ' ')}</option>)}
                  </select>
                ) : (
                  <input
                    name={field.name}
                    type={field.type || 'text'}
                    value={form[field.name] ?? ''}
                    required={field.required}
                    min={field.min}
                    step={field.step}
                    maxLength={field.name === 'phone' ? 30 : undefined}
                    onChange={(event) => setForm({ ...form, [field.name]: event.target.value })}
                  />
                )}
              </label>
            ))}
            {error && <p className="message error-message" role="alert">{error}</p>}
            {notice && <p className="message success-message" role="status">{notice}</p>}
            <div className="form-actions">
              <button className="button primary-button" type="submit" disabled={saving}>
                {saving ? 'Saving...' : editingId ? 'Save changes' : 'Add record'}
              </button>
              {editingId && <button className="button secondary-button" type="button" onClick={cancelEdit}>Cancel</button>}
            </div>
          </form>
        </section>

        <section className="table-panel" aria-labelledby="table-heading">
          <div className="panel-heading table-heading">
            <h3 id="table-heading">All {section.label.toLowerCase()}</h3>
            {loading && <span className="loading-label">Loading</span>}
          </div>
          <div className="table-scroll">
            <table>
              <thead>
                <tr>
                  {section.columns.map((column) => <th key={column.name}>{column.label}</th>)}
                  <th className="actions-heading">Actions</th>
                </tr>
              </thead>
              <tbody>
                {!loading && rows.map((row) => (
                  <tr key={row.id}>
                    {section.columns.map((column) => <td key={column.name}>{displayValue(row[column.name], column.name)}</td>)}
                    <td className="row-actions">
                      <button className="text-button" type="button" onClick={() => startEdit(row)}>Edit</button>
                      <button className="text-button delete-button" type="button" onClick={() => deleteRow(row)}>Delete</button>
                    </td>
                  </tr>
                ))}
                {!loading && rows.length === 0 && (
                  <tr><td className="empty-state" colSpan={section.columns.length + 1}>No records yet</td></tr>
                )}
                {loading && <tr><td className="empty-state" colSpan={section.columns.length + 1}>Loading records...</td></tr>}
              </tbody>
            </table>
          </div>
        </section>
      </div>
      <footer className="page-footer">Urban Traffic Platform <span>|</span> Record management</footer>
    </main>
  );
}