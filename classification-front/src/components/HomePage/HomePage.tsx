import styles from './styles.module.css';
import DropZone from '../DropZone/DropZone';
import { createContext, useState } from 'react';
import ResultModal from '../ResultModal/ResultModal';
import { ArrowRepeat } from 'react-bootstrap-icons';

interface HomeContextModel {
  isLoading: boolean;
  setIsLoading: Function;
  onError: boolean;
  setOnError: Function;
  result: number | undefined;
  setResult: Function;
  image: string | undefined;
  setImage: Function;
}

export const HomeContext = createContext<HomeContextModel>({
  isLoading: false,
  setIsLoading: () => {},
  result: undefined,
  setResult: () => {},
  onError: false,
  setOnError: () => {},
  image: undefined,
  setImage: () => {}
});

export default function HomePage() {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [onError, setOnError] = useState<boolean>(false);
  const [result, setResult] = useState<number | undefined>(undefined);
  const [image, setImage] = useState<string | undefined>(undefined);

  return (
    <HomeContext.Provider
      value={{
        isLoading: isLoading,
        setIsLoading: setIsLoading,
        result: result,
        setResult: setResult,
        onError: onError,
        setOnError: setOnError,
        image: image,
        setImage: setImage
      }}
    >
      <Header />
      {isLoading ? <LoadingComponent /> : <DropZone />}
      <ResultModal />
    </HomeContext.Provider>
  );
}

function Header() {
  return (
    <header className={styles.header}>
      <h1>Clasificación de imagenes</h1>
      <h2>Integrantes</h2>
      <p>
        Andres Felipe Amezquita Gordillo, David Orlando Rodriguez Vargas, Sergio
        Andrey Suarez Bonilla
      </p>
    </header>
  );
}

function LoadingComponent() {
  return (
    <div className={styles.loading_wrapper}>
      <ArrowRepeat className={styles.loading_component} />;
    </div>
  );
}
