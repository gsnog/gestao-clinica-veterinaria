import { useEffect } from 'react';

function useBodyClass(className) {
  useEffect(() => {
    const previous = document.body.className;
    document.body.className = className || '';

    return () => {
      document.body.className = previous;
    };
  }, [className]);
}

export default useBodyClass;
