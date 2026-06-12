function SearchBar({ value, onChange, placeholder, id }) {
  return (
    <div className="search-bar">
      <input
        id={id}
        type="text"
        placeholder={placeholder}
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </div>
  );
}

export default SearchBar;
