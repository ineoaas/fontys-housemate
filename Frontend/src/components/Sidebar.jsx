const PAGES = [
  { id: 'dashboard', label: 'Dashboard' },
  { id: 'chores', label: 'Chores' },
]

function Sidebar({ currentPage, onNavigate }) {
  return (
    <nav>
      <ul>
        {PAGES.map((page) => (
          <li key={page.id}>
            <button
              onClick={() => onNavigate(page.id)}
              aria-current={page.id === currentPage ? 'page' : undefined}
            >
              {page.label}
            </button>
          </li>
        ))}
      </ul>
    </nav>
  )
}

export default Sidebar
